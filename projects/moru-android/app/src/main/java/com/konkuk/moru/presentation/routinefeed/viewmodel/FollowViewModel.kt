package com.konkuk.moru.presentation.routinefeed.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.core.datastore.RoutineSyncBus
import com.konkuk.moru.core.datastore.SocialMemory
import com.konkuk.moru.data.dto.response.Follow.FollowCursorDto
import com.konkuk.moru.data.mapper.toUi
import com.konkuk.moru.domain.repository.RoutineUserRepository
import com.konkuk.moru.domain.repository.SocialRepository
import com.konkuk.moru.presentation.routinefeed.data.FollowUser
import com.konkuk.moru.presentation.routinefeed.screen.follow.FollowUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val socialRepository: SocialRepository,
    private val userRepository: RoutineUserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowUiState())
    val uiState = _uiState.asStateFlow()

    private val _followResult = MutableSharedFlow<Pair<String, Boolean>>(extraBufferCapacity = 1)
    val followResult = _followResult.asSharedFlow()

    // 페이징 커서
    private var followersCursor: FollowCursorDto? = null
    private var followingsCursor: FollowCursorDto? = null

    private val ownerUserId: String = savedStateHandle["userId"] ?: ""

    // 내 아이디 캐시
    private var myId: String? = null

    // ===== TTL 가드 =====
    private data class FollowStamp(
        val wantFollow: Boolean,
        val mark: TimeMark = TimeSource.Monotonic.markNow()
    )

    private val followStampMap = ConcurrentHashMap<String, FollowStamp>()
    private val PROTECT_TTL = 2.seconds

    /** 서버 isFollow 값에 낙관값(TTL 내)을 오버레이 */
    private fun guardFollow(userId: String, serverIsFollowing: Boolean): Boolean {
        val s = followStampMap[userId] ?: return serverIsFollowing
        return if (s.mark.elapsedNow() <= PROTECT_TTL) s.wantFollow else serverIsFollowing
    }

    // ===== in-flight 가드(중복 요청 차단) =====
    private val inFlightSet = ConcurrentHashMap.newKeySet<String>()

    init {
        viewModelScope.launch {
            ensureMeId()
            loadOwnerName()
            loadFollowers(refresh = true)
            loadFollowings(refresh = true)
        }

        // ✅ 팔로우 이벤트 수신 → 내 팔로잉 화면이면 즉시 추가/제거
        viewModelScope.launch {
            RoutineSyncBus.events.collect { e ->
                if (e is RoutineSyncBus.Event.Follow) {
                    val me = myId ?: ensureMeId()
                    // 항상 현재 목록에 있는 항목은 즉시 isFollowing 덮어쓰기
                    _uiState.update { s ->
                        s.copy(
                            followers = s.followers.map { u ->
                                if (u.id == e.userId) u.copy(isFollowing = e.isFollowing) else u
                            },
                            followings = s.followings.map { u ->
                                if (u.id == e.userId) u.copy(isFollowing = e.isFollowing) else u
                            }
                        )
                    }

                    // ⬇️ 내 팔로잉 화면일 때만 “없던 유저 추가 / 언팔 제거”
                    if (me != null && ownerUserId == me) {
                        if (e.isFollowing) {
                            val already = _uiState.value.followings.any { it.id == e.userId }
                            if (!already) {
                                // 프로필 한 번 떠서 카드 정보 생성
                                val ui = fetchFollowUserUi(e.userId)
                                _uiState.update { s ->
                                    s.copy(followings = listOf(ui) + s.followings)
                                }
                            }
                        } else {
                            // 언팔이면 목록에서 제거
                            _uiState.update { s ->
                                s.copy(followings = s.followings.filterNot { it.id == e.userId })
                            }
                        }
                    }
                }
            }
        }
    }

    /** 한 번만 내 ID 조회 후 UI에 반영 */
    private suspend fun ensureMeId(): String? {
        if (myId != null) return myId
        myId = runCatching { userRepository.getMe().id }.getOrNull()
        _uiState.update { it.copy(myId = myId) }
        return myId
    }

    private suspend fun loadOwnerName() {
        if (ownerUserId.isBlank() || ownerUserId == "null") return

        val ownerName = runCatching {
            userRepository.getUserProfile(ownerUserId).nickname
        }.getOrNull()

        if (!ownerName.isNullOrBlank()) {
            _uiState.update { it.copy(ownerName = ownerName) }
        }
    }

    /** SocialMemory/TTL/자기자신 숨김 고려한 오버레이 */
    private fun overlayFollowFlags(base: List<FollowUser>): List<FollowUser> {
        val me = myId
        return base.map { u ->
            val local = SocialMemory.getFollow(u.id)
            val guarded = guardFollow(u.id, u.isFollowing)
            val fixed = local ?: guarded
            val finalIsFollow = if (me != null && u.id == me) false else fixed
            u.copy(isFollowing = finalIsFollow)
        }
    }

    fun loadFollowers(refresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFollowers = true) }

            val lastNick = if (refresh) null else followersCursor?.nickname
            val lastId = if (refresh) null else followersCursor?.userId

            runCatching { socialRepository.getFollowers(ownerUserId, lastNick, lastId, limit = 10) }
                .onSuccess { res ->
                    val mapped = res.content.map { it.toUi() }
                    val overlaid = overlayFollowFlags(mapped)
                    _uiState.update {
                        it.copy(
                            followers = if (refresh) overlaid else it.followers + overlaid,
                            isLoadingFollowers = false
                        )
                    }
                    followersCursor = res.nextCursor
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingFollowers = false) }
                }
        }
    }

    fun loadFollowings(refresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFollowings = true) }

            val lastNick = if (refresh) null else followingsCursor?.nickname
            val lastId = if (refresh) null else followingsCursor?.userId

            runCatching { socialRepository.getFollowing(ownerUserId, lastNick, lastId, limit = 10) }
                .onSuccess { res ->
                    val base = res.content.map { it.toUi() }
                    val me = myId // ⚠️ suspend 호출 금지(이미 init에서 확보)
                    // 내가 내 페이지를 볼 땐 팔로잉 목록은 강제로 true
                    val forced = if (me != null && ownerUserId == me) {
                        base.map { it.copy(isFollowing = true) }
                    } else base
                    val overlaid = overlayFollowFlags(forced)

                    _uiState.update {
                        it.copy(
                            followings = if (refresh) overlaid else it.followings + overlaid,
                            isLoadingFollowings = false
                        )
                    }
                    followingsCursor = res.nextCursor
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingFollowings = false) }
                }
        }
    }

    fun toggleFollow(clickedUser: FollowUser) {
        val me = myId
        if (clickedUser.id == me) return   // ✅ 자기 자신 보호

        // ✅ 중복 요청 가드
        if (!inFlightSet.add(clickedUser.id)) return
        _uiState.update { it.copy(inFlight = it.inFlight + clickedUser.id) }

        val before = _uiState.value
        val wantFollow = !clickedUser.isFollowing

        // ✅ 낙관 UI
        _uiState.update { current ->
            current.copy(
                followers = current.followers.map {
                    if (it.id == clickedUser.id) it.copy(isFollowing = wantFollow) else it
                },
                followings = if (wantFollow) {
                    if (current.followings.any { it.id == clickedUser.id }) current.followings
                    else listOf(clickedUser.copy(isFollowing = true)) + current.followings
                } else {
                    current.followings.filterNot { it.id == clickedUser.id }
                }
            )
        }

        // ✅ 메모리/스탬프
        SocialMemory.setFollow(clickedUser.id, wantFollow)
        followStampMap[clickedUser.id] = FollowStamp(wantFollow)

        viewModelScope.launch {
            runCatching {
                if (wantFollow) socialRepository.follow(clickedUser.id)
                else socialRepository.unfollow(clickedUser.id)
            }.onSuccess {
                _followResult.tryEmit(clickedUser.id to wantFollow)
                RoutineSyncBus.publish(
                    RoutineSyncBus.Event.Follow(
                        userId = clickedUser.id,
                        isFollowing = wantFollow
                    )
                )
            }.onFailure {
                // 롤백
                _uiState.value = before
                val rb = before.followers.find { it.id == clickedUser.id }?.isFollowing ?: false
                SocialMemory.setFollow(clickedUser.id, rb)
                followStampMap.remove(clickedUser.id)
            }.also {
                inFlightSet.remove(clickedUser.id)
                _uiState.update { st -> st.copy(inFlight = st.inFlight - clickedUser.id) }
            }
        }
    }

    private suspend fun fetchFollowUserUi(targetUserId: String): FollowUser {
        val d = runCatching { userRepository.getUserProfile(targetUserId) }.getOrNull()
        return if (d != null) {
            FollowUser(
                id = d.id,
                profileImageUrl = d.profileImageUrl ?: "",
                username = d.nickname,
                bio = d.bio ?: "",
                isFollowing = true
            )
        } else {
            // 프로필 실패 시 최소 정보로라도 추가(이름은 나중에 갱신돼도 OK)
            FollowUser(
                id = targetUserId,
                profileImageUrl = "",
                username = "",
                bio = "",
                isFollowing = true
            )
        }
    }

}
