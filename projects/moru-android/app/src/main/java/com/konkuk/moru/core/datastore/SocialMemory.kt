package com.konkuk.moru.core.datastore

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.ConcurrentHashMap

/**
 * 좋아요/스크랩/팔로우의 최신 상태를 화면 간에 일관되게 공유하는 전역 메모리.
 * - 서버 응답이 늦게 와서 UI가 되돌아가는 것을 방지하기 위해 updatedAt 기준으로 신뢰도 관리
 */
object SocialMemory {

    data class RoutineState(
        val likeCount: Int? = null,
        val isLiked: Boolean? = null,
        val scrapCount: Int? = null,
        val isScrapped: Boolean? = null,
        val updatedAt: Long = System.currentTimeMillis()
    )

    private val routineMap = ConcurrentHashMap<String, RoutineState>()
    private val followMap = ConcurrentHashMap<String, Pair<Boolean, Long>>() // userId -> (isFollowing, updatedAt)

    // 상태 갱신 브로드캐스트 (선택적으로 구독해 UI에 반영 가능)
    private val _signals = MutableSharedFlow<Unit>(extraBufferCapacity = 64)
    val signals = _signals.asSharedFlow()

    // --- Routine (like/scrap) ---
    fun setLike(routineId: String, isLiked: Boolean, likeCount: Int) {
        routineMap.merge(routineId, RoutineState(likeCount, isLiked, null, null)) { old, new ->
            old.merge(new)
        }
        _signals.tryEmit(Unit)
    }

    fun setScrap(routineId: String, isScrapped: Boolean, scrapCount: Int? = null) {
        routineMap.merge(routineId, RoutineState(null, null, scrapCount, isScrapped)) { old, new ->
            old.merge(new)
        }
        _signals.tryEmit(Unit)
    }

    fun getRoutine(routineId: String): RoutineState? = routineMap[routineId]

    // --- Follow ---
    fun setFollow(userId: String, isFollowing: Boolean) {
        followMap[userId] = isFollowing to System.currentTimeMillis()
        _signals.tryEmit(Unit)
    }

    fun getFollow(userId: String): Boolean? = followMap[userId]?.first

    // --- Utils ---
    private fun RoutineState.merge(other: RoutineState): RoutineState {
        // 최신(updatedAt) 값을 우선 적용
        fun pick(a: Any?, b: Any?, aTime: Long, bTime: Long) =
            when {
                b != null && bTime >= aTime -> b
                else -> a
            }
        return RoutineState(
            likeCount = pick(this.likeCount, other.likeCount, this.updatedAt, other.updatedAt) as Int?,
            isLiked = pick(this.isLiked, other.isLiked, this.updatedAt, other.updatedAt) as Boolean?,
            scrapCount = pick(this.scrapCount, other.scrapCount, this.updatedAt, other.updatedAt) as Int?,
            isScrapped = pick(this.isScrapped, other.isScrapped, this.updatedAt, other.updatedAt) as Boolean?,
            updatedAt = maxOf(this.updatedAt, other.updatedAt)
        )
    }
}