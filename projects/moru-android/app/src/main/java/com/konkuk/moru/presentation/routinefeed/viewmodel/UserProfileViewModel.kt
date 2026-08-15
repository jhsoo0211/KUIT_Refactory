package com.konkuk.moru.presentation.routinefeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.core.datastore.RoutineSyncBus
import com.konkuk.moru.core.datastore.SocialMemory
import com.konkuk.moru.data.mapper.toRoutineModel
import com.konkuk.moru.data.mapper.toUiRoutine
import com.konkuk.moru.domain.repository.RoutineFeedRepository
import com.konkuk.moru.domain.repository.SocialRepository
import com.konkuk.moru.domain.repository.RoutineUserRepository
import com.konkuk.moru.presentation.routinefeed.data.UserProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: RoutineUserRepository,
    private val socialRepository: SocialRepository,
    private val routineFeedRepository: RoutineFeedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    // ===== TTL 가드 스탬프 =====
    private data class FollowStamp(
        val wantFollow: Boolean,
        val mark: TimeMark = TimeSource.Monotonic.markNow()
    )

    private data class LikeStamp(
        val routineId: String,
        val wantLike: Boolean,
        val expectedLikes: Int,
        val mark: TimeMark = TimeSource.Monotonic.markNow()
    )

    private val followStampMap = ConcurrentHashMap<String, FollowStamp>()
    private val likeStampMap = ConcurrentHashMap<String, LikeStamp>()
    private val PROTECT_TTL = 2.seconds

    private val followGate = java.util.concurrent.atomic.AtomicBoolean(false)

    init {
        val userId: String? = savedStateHandle["userId"]
        viewModelScope.launch {
            val domain = runCatching {
                val argUserId: String? = savedStateHandle["userId"]
                if (argUserId.isNullOrBlank()) {
                    val me = userRepository.getMe()
                    userRepository.getUserProfile(me.id)
                } else {
                    userRepository.getUserProfile(argUserId)
                }
            }.getOrElse { e ->
                _uiState.update {
                    it.copy(
                        nickname = "알 수 없는 사용자",
                        bio = "사용자 정보를 불러오는 데 실패했습니다. (${e.message ?: "알 수 없는 오류"})",
                        routineCount = 0,
                        followerCount = 0,
                        followingCount = 0,
                        runningRoutines = emptyList(),
                        userRoutines = emptyList()
                    )
                }
                return@launch
            }

            _uiState.update { prev ->
                prev.copy(
                    userId = domain.id,
                    isMe = domain.isMe,
                    profileImageUrl = domain.profileImageUrl,
                    nickname = domain.nickname,
                    bio = domain.bio ?: "",
                    routineCount = domain.routineCount,
                    followerCount = domain.followerCount,
                    followingCount = domain.followingCount,
                    isFollowing = false, // 초기엔 false, 이후 보정
                    runningRoutines = domain.currentRoutine?.let {
                        listOf(it.toUiRoutine(domain.id, domain.nickname, domain.profileImageUrl))
                    } ?: emptyList(),
                    userRoutines = domain.routines.map {
                        it.toUiRoutine(
                            domain.id,
                            domain.nickname,
                            domain.profileImageUrl
                        ) // ✅ 프로필 소유자 정보 넣음
                    }
                )
            }

            // 초기 팔로잉 여부 보정 (+ TTL 가드)
            refreshInitialFollowing()

            // 다른 화면 변화 동기화
            RoutineSyncBus.events.collectLatest { e ->
                when (e) {
                    is RoutineSyncBus.Event.Like -> {
                        _uiState.update { s ->
                            s.copy(
                                runningRoutines = s.runningRoutines.map { r ->
                                    if (r.routineId == e.routineId) r.copy(
                                        isLiked = e.isLiked,
                                        likes = e.likeCount
                                    ) else r
                                },
                                userRoutines = s.userRoutines.map { r ->
                                    if (r.routineId == e.routineId) r.copy(
                                        isLiked = e.isLiked,
                                        likes = e.likeCount
                                    ) else r
                                }
                            )
                        }
                    }

                    is RoutineSyncBus.Event.Scrap -> {
                        _uiState.update { s ->
                            s.copy(
                                runningRoutines = s.runningRoutines.map { r ->
                                    if (r.routineId == e.routineId) r.copy(isBookmarked = e.isScrapped) else r
                                },
                                userRoutines = s.userRoutines.map { r ->
                                    if (r.routineId == e.routineId) r.copy(isBookmarked = e.isScrapped) else r
                                }
                            )
                        }
                    }

                    is RoutineSyncBus.Event.Follow -> {
                        val targetId = _uiState.value.userId
                        if (targetId != null && targetId == e.userId) {
                            applyExternalFollow(e.isFollowing)
                        }
                    }

                    // [추가] 내 루틴 복사/삭제 등으로 '내 프로필'이 바뀐 경우 최신화
                    RoutineSyncBus.Event.MyRoutinesChanged -> {
                        if (_uiState.value.isMe == true) {
                            // UX용 선반영
                            _uiState.update {
                                it.copy(
                                    routineCount = (it.routineCount + 1).coerceAtLeast(
                                        0
                                    )
                                )
                            }
                            // 서버 확정값으로 맞추기
                            viewModelScope.launch { refreshUserProfile() }
                        }
                    }
                }
            }
        }
    }


    // ===== 팔로우 =====
    private suspend fun refreshInitialFollowing() {
        val targetId = _uiState.value.userId ?: return

        // 내 프로필이면 팔로우 비활성 + 버튼 숨김용
        if (_uiState.value.isMe == true) {
            _uiState.update { it.copy(isFollowing = false) }
            return
        }

        // 전역 메모리 우선
        SocialMemory.getFollow(targetId)?.let { mem ->
            _uiState.update { it.copy(isFollowing = mem) }
            return
        }

        val meId = runCatching { userRepository.getMe().id }.getOrNull().orEmpty()
        if (meId.isBlank()) return

        val page = runCatching {
            socialRepository.getFollowing(
                userId = meId,
                lastNickname = null,
                lastUserId = null,
                limit = 100
            )
        }.getOrNull()

        var serverFollowing = page?.content?.any { it.userId == targetId } == true

        // TTL 내 낙관값 우선
        followStampMap[targetId]?.let { st ->
            if (st.mark.elapsedNow() <= PROTECT_TTL) {
                serverFollowing = st.wantFollow
            }
        }

        _uiState.update { it.copy(isFollowing = serverFollowing) }
        SocialMemory.setFollow(targetId, serverFollowing)
    }

    fun toggleFollow() {
        if (!followGate.compareAndSet(false, true)) return  // ✅ 동시 재진입 차단
        val before = _uiState.value
        val targetUserId: String = before.userId ?: run { followGate.set(false); return }
        if (targetUserId.isBlank() || before.isMe == true) {
            followGate.set(false); return
        }

        val wantFollow = !before.isFollowing

        _uiState.update {
            it.copy(
                isFollowLoading = true,
                isFollowing = wantFollow,
                followerCount = (it.followerCount + if (wantFollow) 1 else -1).coerceAtLeast(0)
            )
        }

        SocialMemory.setFollow(targetUserId, wantFollow)
        followStampMap[targetUserId] = FollowStamp(wantFollow)

        viewModelScope.launch {
            runCatching {
                if (wantFollow) socialRepository.follow(targetUserId) else socialRepository.unfollow(
                    targetUserId
                )
            }.onSuccess {
                _uiState.update { it.copy(isFollowLoading = false) }
                RoutineSyncBus.publish(
                    RoutineSyncBus.Event.Follow(
                        userId = targetUserId,
                        isFollowing = wantFollow
                    )
                )
                followStampMap.remove(targetUserId)
            }.onFailure {
                _uiState.value = before.copy(isFollowLoading = false)
                SocialMemory.setFollow(targetUserId, before.isFollowing)
                followStampMap.remove(targetUserId)
            }.also {
                followGate.set(false)  // ✅ 해제
            }
        }
    }

    // ===== 좋아요 =====
    private fun mergeLikesAfterToggle(beforeLikes: Int, wantLike: Boolean, serverLikes: Int): Int {
        val expected = (beforeLikes + if (wantLike) 1 else -1).coerceAtLeast(0)
        return when {
            wantLike && serverLikes < expected -> expected
            !wantLike && serverLikes > expected -> expected
            else -> serverLikes
        }
    }

    fun toggleLike(routineId: String) {
        val before = _uiState.value

        // 낙관 UI 반영
        val after = before.copy(
            runningRoutines = before.runningRoutines.map { r ->
                if (r.routineId == routineId) {
                    val wantLike = !r.isLiked
                    val bumped = (r.likes + if (wantLike) 1 else -1).coerceAtLeast(0)
                    // 메모리 즉시 반영
                    SocialMemory.setLike(routineId, wantLike, bumped)
                    r.copy(isLiked = wantLike, likes = bumped)
                } else r
            },
            userRoutines = before.userRoutines.map { r ->
                if (r.routineId == routineId) {
                    val wantLike = !r.isLiked
                    val bumped = (r.likes + if (wantLike) 1 else -1).coerceAtLeast(0)
                    SocialMemory.setLike(routineId, wantLike, bumped)
                    r.copy(isLiked = wantLike, likes = bumped)
                } else r
            }
        )
        _uiState.value = after

        // 스탬프 기록(둘 중 하나에서 찾으면 동일)
        val cur = (after.runningRoutines + after.userRoutines).first { it.routineId == routineId }
        likeStampMap[routineId] =
            LikeStamp(routineId, wantLike = cur.isLiked, expectedLikes = cur.likes)

        viewModelScope.launch {
            runCatching {
                if (cur.isLiked) routineFeedRepository.addLike(routineId)
                else routineFeedRepository.removeLike(routineId)
                routineFeedRepository.getRoutineDetail(routineId)
            }.onSuccess { fresh ->
                val server = fresh.toRoutineModel()
                // TTL 동안은 낙관값 우선 병합
                val stamp = likeStampMap[routineId]
                val mergedLikes = if (stamp != null && stamp.mark.elapsedNow() <= PROTECT_TTL) {
                    mergeLikesAfterToggle(
                        beforeLikes = before.userRoutines.plus(before.runningRoutines)
                            .firstOrNull { it.routineId == routineId }?.likes ?: server.likes,
                        wantLike = stamp.wantLike,
                        serverLikes = server.likes
                    )
                } else server.likes

                val mergedIsLiked = stamp?.let { s ->
                    if (s.mark.elapsedNow() <= PROTECT_TTL) s.wantLike else server.isLiked
                } ?: server.isLiked

                // 확정값 메모리 고정
                SocialMemory.setLike(server.routineId, mergedIsLiked, mergedLikes)

                // 화면 갱신
                _uiState.update { s ->
                    s.copy(
                        runningRoutines = s.runningRoutines.map {
                            if (it.routineId == routineId) it.copy(
                                isLiked = mergedIsLiked,
                                likes = mergedLikes
                            ) else it
                        },
                        userRoutines = s.userRoutines.map {
                            if (it.routineId == routineId) it.copy(
                                isLiked = mergedIsLiked,
                                likes = mergedLikes
                            ) else it
                        }
                    )
                }
                RoutineSyncBus.publish(
                    RoutineSyncBus.Event.Like(
                        server.routineId,
                        mergedIsLiked,
                        mergedLikes
                    )
                )
                likeStampMap.remove(routineId)
            }.onFailure {
                _uiState.value = before // 롤백
                // 메모리 롤백
                val rb =
                    (before.runningRoutines + before.userRoutines).firstOrNull { it.routineId == routineId }
                if (rb != null) SocialMemory.setLike(rb.routineId, rb.isLiked, rb.likes)
                likeStampMap.remove(routineId)
            }
        }
    }

    fun toggleRunningRoutineExpansion() {
        _uiState.update { it.copy(isRunningRoutineExpanded = !it.isRunningRoutineExpanded) }
    }

    fun applyExternalFollow(isFollowing: Boolean) {
        _uiState.update { s ->
            val delta = when {
                isFollowing && !s.isFollowing -> +1
                !isFollowing && s.isFollowing -> -1
                else -> 0
            }
            s.copy(
                isFollowing = isFollowing,
                followerCount = (s.followerCount + delta).coerceAtLeast(0)
            )
        }
        // 메모리 동기화(프로필 대상만)
        _uiState.value.userId?.let { SocialMemory.setFollow(it, isFollowing) }
    }

    //유저 프로필 부분 업데이트
    private suspend fun refreshUserProfile() {
        val targetId = _uiState.value.userId ?: return
        runCatching { userRepository.getUserProfile(targetId) }
            .onSuccess { domain ->
                _uiState.update { prev ->
                    prev.copy(
                        userId = domain.id,
                        isMe = domain.isMe,
                        profileImageUrl = domain.profileImageUrl,
                        nickname = domain.nickname,
                        bio = domain.bio.orEmpty(),
                        routineCount = domain.routineCount,
                        followerCount = domain.followerCount,
                        followingCount = domain.followingCount,
                        runningRoutines = domain.currentRoutine?.let {
                            listOf(
                                it.toUiRoutine(
                                    domain.id,
                                    domain.nickname,
                                    domain.profileImageUrl
                                )
                            )
                        } ?: emptyList(),
                        userRoutines = domain.routines.map {
                            it.toUiRoutine(domain.id, domain.nickname, domain.profileImageUrl)
                        }
                    )
                }
            }
    }


}

