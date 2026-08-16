package com.konkuk.moru.core.datastore

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object RoutineSyncBus {

    sealed class Event {
        data class Like(val routineId: String, val isLiked: Boolean, val likeCount: Int) : Event()
        data class Scrap(val routineId: String, val isScrapped: Boolean) : Event()
        data class Follow(val userId: String, val isFollowing: Boolean) : Event()

        // ========================= [추가] 내 루틴 변동 브로드캐스트 =========================
        data object MyRoutinesChanged : Event() // ← 이전 코드의 MineChanged 대신 이 이름으로 고정
        // ================================================================================
    }
    

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    fun publish(event: Event) {
        _events.tryEmit(event) // [추가]
    }
}