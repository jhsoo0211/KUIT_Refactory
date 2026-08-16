package com.konkuk.moru.presentation.routinefeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.core.util.DateFormatters
import com.konkuk.moru.data.model.FavoriteTag
import com.konkuk.moru.data.model.Page
import com.konkuk.moru.data.model.RoutineSummary
import com.konkuk.moru.data.model.TagItem
import com.konkuk.moru.domain.repository.SearchRepository
import com.konkuk.moru.domain.repository.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepository
) : ViewModel() {

    data class UiState(
        val query: String = "",
        val suggestions: List<String> = emptyList(),
        val selectedTags: List<String> = emptyList(), // UI는 "#태그" 형식
        val sortType: SortType = SortType.LATEST,
        val results: List<RoutineSummary> = emptyList(),
        val page: Int = 0,
        val canLoadMore: Boolean = false,
        val isLoading: Boolean = false,
        val recentSearches: List<RecentItem> = emptyList(),
        val favoriteTags: List<FavoriteTag> = emptyList(),
        val allTags: List<TagItem> = emptyList(),
    )

    data class RecentItem(val id: String, val text: String, val date: String)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private var suggestJob: Job? = null

    // ✅ [추가] 루틴이 모든 선택 태그를 포함하는지(AND) -> 서버에서 태그 모아서 따로 필터링하는거 없어서 안드에서 해결
    private fun RoutineSummary.hasAllTags(selected: List<String>): Boolean {
        if (selected.isEmpty()) return true
        val mine = tags.map { it.lowercase() }
        return selected.all { sel -> mine.contains(sel.removePrefix("#").lowercase()) }
    }


    init {
        refreshHistories()
        refreshFavoriteTags()
    }

    // ------------------- 입력 & 자동완성 -------------------
    fun onQueryChange(new: String) {
        _uiState.value = _uiState.value.copy(query = new)
        suggestJob?.cancel()
        suggestJob = viewModelScope.launch {
            delay(250)
            runCatching { repo.getTitleSuggestions(new) }
                .onSuccess { _uiState.value = _uiState.value.copy(suggestions = it) }
        }
    }

    // ------------------- 태그 선택/삭제 -------------------
    fun addTag(tag: String) {
        val normalized = if (tag.startsWith("#")) tag else "#$tag"
        if (normalized !in _uiState.value.selectedTags) {
            _uiState.value =
                _uiState.value.copy(selectedTags = _uiState.value.selectedTags + normalized)
            performSearch(resetPage = true) // ✅ 변경: 추가 즉시 재검색
        }
    }

    fun removeTag(tag: String) {
        _uiState.value = _uiState.value.copy(selectedTags = _uiState.value.selectedTags - tag)
        // 태그 변경 시 재검색 (옵션)
        performSearch(resetPage = true)
    }

    // ------------------- 정렬 변경 -------------------
    fun setSort(sort: SortType) {
        if (_uiState.value.sortType != sort) {
            _uiState.value = _uiState.value.copy(sortType = sort)
            performSearch(resetPage = true) // 변경: 정렬 변경 시 재검색
        }
    }

    // ------------------- 검색 실행 -------------------
    fun performSearch(resetPage: Boolean = true) {
        val current = _uiState.value
        viewModelScope.launch {
            if (resetPage) {
                _uiState.value = current.copy(isLoading = true, page = 0, results = emptyList())
            } else {
                _uiState.value = current.copy(isLoading = true)
            }

            val selected = _uiState.value.selectedTags // 최신 상태 사용
            val tagNames = selected.map { it.removePrefix("#") }
                .ifEmpty { null } // 서버엔 그대로 보내서 1차 축소(OR일 수 있음)

            // ✅ 목표 수량 (한 번 그려줄 양)
            val want = 20
            val needAnd = selected.isNotEmpty()

            // ✅ 수집 버퍼
            val acc = mutableListOf<RoutineSummary>()

            // ✅ 불러올 서버 페이지 인덱스
            var pageToLoad = if (resetPage) 0 else current.page + 1
            var serverHasMore: Boolean
            var loop = 0 // 무한루프 방지

            do {
                val page: Page<RoutineSummary> = runCatching {
                    repo.searchRoutines(
                        titleKeyword = _uiState.value.query.ifBlank { null },
                        tagNames = tagNames,
                        sortType = _uiState.value.sortType,
                        page = pageToLoad,
                        size = want
                    )
                }.getOrElse {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    return@launch
                }

                // ✅ 핵심: 클라에서 AND 재필터링
                val chunk = if (needAnd) page.content.filter { it.hasAllTags(selected) }
                else page.content
                acc.addAll(chunk)

                serverHasMore = !page.isLast
                pageToLoad = page.page + 1
                loop++
                // ▶ 충분히 모았으면 중단. 모자라면 다음 서버 페이지 더 당겨옴.
            } while (acc.size < want && serverHasMore && loop < 5)

            val newList = if (resetPage) acc else current.results + acc

            _uiState.value = _uiState.value.copy(
                results = newList,
                page = if (resetPage) 0 else current.page + 1, // 클라 표시용 페이지
                canLoadMore = serverHasMore,                   // 서버가 더 있으면 더보기 가능
                isLoading = false
            )
        }
    }

    fun loadMore() {
        if (_uiState.value.canLoadMore && !_uiState.value.isLoading) {
            performSearch(resetPage = false)
        }
    }

    // ------------------- 최근 검색어 -------------------
    fun refreshHistories() {
        viewModelScope.launch {
            runCatching { repo.getRoutineNameHistories() }
                .onSuccess { list ->
                    _uiState.value = _uiState.value.copy(
                        recentSearches = list.map {
                            RecentItem(
                                id = it.id,
                                text = it.keyword,
                                date = DateFormatters.isoToMonthDayDot(it.createdAtIso)
                            )
                        }
                    )
                }
        }
    }

    fun deleteRecent(id: String) {
        viewModelScope.launch {
            runCatching { repo.deleteHistory(id) }
                .onSuccess { refreshHistories() }
        }
    }

    fun deleteAllRecent() {
        viewModelScope.launch {
            runCatching { repo.deleteAllHistories() }
                .onSuccess { refreshHistories() }
        }
    }


    // 전체 태그

    fun refreshAllTags() {
        viewModelScope.launch {
            runCatching { repo.getAllTags() }
                .onSuccess { all ->
                    _uiState.value = _uiState.value.copy(allTags = all)
                }
        }
    }


    // ------------------- 관심 태그 -------------------
    fun refreshFavoriteTags() {
        viewModelScope.launch {
            runCatching { repo.getFavoriteTags() }
                .onSuccess { _uiState.value = _uiState.value.copy(favoriteTags = it) }
        }
    }
}