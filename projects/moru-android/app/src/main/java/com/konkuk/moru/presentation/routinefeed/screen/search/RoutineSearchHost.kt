package com.konkuk.moru.presentation.routinefeed.screen.search

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.presentation.routinefeed.viewmodel.SearchViewModel
import com.konkuk.moru.ui.theme.MORUTheme

/**
 * 검색 화면의 상태를 정의하는 Enum Class
 */
internal enum class SearchMode {
    INITIAL,            // 최초 진입 (루틴명 검색)
    ROUTINE_NAME_RESULT,// 루틴명 검색 결과
    TAG_SEARCH          // 태그 검색/관리
}

@Composable
fun RoutineSearchHost(
    navController: NavHostController
) {

    val vm: SearchViewModel = hiltViewModel()
    val ui by vm.uiState.collectAsState()

    var searchMode by remember { mutableStateOf(SearchMode.INITIAL) }
    var searchQuery by remember { mutableStateOf("") }

    val recentSearches = ui.recentSearches.map { it.text }

    val selectedTags = remember { mutableStateListOf<String>() }
    // 변경: VM 상태로 동기화
    LaunchedEffect(ui.selectedTags) {
        selectedTags.clear()
        selectedTags.addAll(ui.selectedTags)
    }

    // 뒤로가기 로직 (시스템/툴바 모두 공통)
    val handleNavigateBack: () -> Unit = {

        when (searchMode) {
            SearchMode.ROUTINE_NAME_RESULT, SearchMode.TAG_SEARCH -> {
                if (selectedTags.isNotEmpty()) {
                    // 선택된 태그를 호출자에게 전달
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedTagsResult", ArrayList(selectedTags))

                    val popped = navController.popBackStack() // ← 이전 화면으로
                    if (!popped) {
                        navController.graph.startDestinationRoute?.let { start ->
                            navController.navigate(start)
                        }
                    }
                } else {
                    searchMode = SearchMode.INITIAL
                    searchQuery = ""
                    selectedTags.clear()
                }
            }

            SearchMode.INITIAL -> {
                val popped = navController.popBackStack()
                if (!popped) {
                    navController.graph.startDestinationRoute?.let { start ->
                        navController.navigate(start)
                    }
                }
            }
        }
    }

    // 시스템 뒤로가기 처리
    BackHandler {
        handleNavigateBack()
    }

    when (searchMode) {
        SearchMode.INITIAL -> {
            RoutineSearchInitialScreen(
                // 변경: 최근 검색과 삭제/전체삭제 -> VM 사용
                recentSearches = ui.recentSearches.map { it.text },
                onPerformSearch = { query ->
                    searchQuery = query
                    vm.onQueryChange(query) // 변경: 검색어 상태 전달
                    vm.performSearch(resetPage = true) // 변경: 서버 검색 실행
                    searchMode = SearchMode.ROUTINE_NAME_RESULT
                },
                onDeleteRecentSearch = { text ->
                    // 변경: id를 찾아서 삭제
                    ui.recentSearches.firstOrNull { it.text == text }?.id?.let(vm::deleteRecent)
                },
                onDeleteAllRecentSearches = vm::deleteAllRecent,
                onNavigateToTagSearch = { searchMode = SearchMode.TAG_SEARCH },
                onNavigateBack = handleNavigateBack
            )
        }

        SearchMode.ROUTINE_NAME_RESULT -> {
            RoutineNameResultScreen(
                query = ui.query, // 변경
                onQueryChange = { q -> vm.onQueryChange(q) }, // 변경
                onPerformSearch = { vm.performSearch(resetPage = true) }, // 변경
                selectedTags = ui.selectedTags, // 변경
                onNavigateBack = handleNavigateBack,
                onRoutineClick = { routineId ->
                    navController.navigate(Route.RoutineFeedDetail.createRoute(routineId))
                },
                onNavigateToTagSearch = { searchMode = SearchMode.TAG_SEARCH },
                onDeleteTag = { tag -> vm.removeTag(tag) } // 변경
            )
        }

        SearchMode.TAG_SEARCH -> {
            TagSearchScreen(
                originalQuery = searchQuery,
                onNavigateBack = handleNavigateBack,
                onTagSelected = { selectedTag ->
                    // ✅ 변경: 태그를 VM에 추가하고, 결과 화면으로 이동 + 재검색
                    vm.addTag(selectedTag) // ← "#” 유무는 내부에서 보정되도록 구현
                    searchMode = SearchMode.ROUTINE_NAME_RESULT  // ← 결과 화면으로
                    vm.performSearch(resetPage = true)           // ← 서버 필터링 실행
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun RoutineSearchHostPreview() {
    MORUTheme {
        RoutineSearchHost(navController = rememberNavController())
    }
}
