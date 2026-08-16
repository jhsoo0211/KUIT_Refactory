package com.konkuk.moru.presentation.routinefeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.data.model.NotificationCursor
import com.konkuk.moru.data.model.NotificationItem
import com.konkuk.moru.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: NotificationRepository
) : ViewModel() {

    data class UiState(
        val items: List<NotificationItem> = emptyList(),
        val cursor: NotificationCursor? = null,
        val hasNext: Boolean = true,
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val loadedIds = mutableSetOf<String>()

    fun loadFirstPage(size: Int = 20) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            runCatching { repo.fetchNotifications(cursor = null, size = size) }
                .onSuccess { page ->
                    loadedIds.clear()
                    val dedup = page.items.filter { loadedIds.add(it.id) }
                    _uiState.update {
                        it.copy(
                            items = dedup,
                            cursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isRefreshing = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isRefreshing = false, error = e.message) }
                }
        }
    }

    fun loadNextPage(size: Int = 20) {
        val s = _uiState.value
        if (!s.hasNext || s.isLoadingMore || s.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            runCatching { repo.fetchNotifications(cursor = s.cursor, size = size) }
                .onSuccess { page ->
                    val newItems = page.items.filter { loadedIds.add(it.id) }
                    _uiState.update {
                        it.copy(
                            items = it.items + newItems,
                            cursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoadingMore = false, error = e.message) }
                }
        }
    }

    fun loadAllRemaining(size: Int = 20) {
        val s = _uiState.value
        if (!s.hasNext || s.isLoadingMore || s.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            try {
                var cursor = _uiState.value.cursor
                var hasNext = _uiState.value.hasNext

                while (hasNext) {
                    val page = repo.fetchNotifications(cursor = cursor, size = size)
                    val newItems = page.items.filter { loadedIds.add(it.id) }
                    _uiState.update {
                        it.copy(
                            items = it.items + newItems,
                            cursor = page.nextCursor,
                            hasNext = page.hasNext
                        )
                    }
                    cursor = page.nextCursor
                    hasNext = page.hasNext
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        val currentItems = _uiState.value.items

        // 1. Optimistic Update: UI에서 즉시 아이템 제거
        _uiState.update { state ->
            state.copy(items = state.items.filterNot { it.id == notificationId })
        }

        // 2. 백그라운드에서 서버에 삭제 API 호출
        viewModelScope.launch {
            runCatching { repo.deleteNotification(notificationId) }
                .onFailure { e ->
                    // 실패 시 UI 롤백 및 에러 메시지 표시 (선택적)
                    _uiState.update { state ->
                        state.copy(items = currentItems, error = "삭제에 실패했습니다: ${e.message}")
                    }
                }
        }
    }

}