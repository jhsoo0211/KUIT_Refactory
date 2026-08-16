package com.konkuk.moru.presentation.routinefeed.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.presentation.routinefeed.component.search.RecentSearchItem
import com.konkuk.moru.presentation.routinefeed.component.search.RecentSearchesHeader
import com.konkuk.moru.presentation.routinefeed.component.search.RoutineSearchTopAppBar
import com.konkuk.moru.presentation.routinefeed.component.search.TagSearchBar
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
internal fun RoutineSearchInitialScreen(
    recentSearches: List<String>, // VM에서 전달받음
    onPerformSearch: (String) -> Unit,
    onNavigateToTagSearch: () -> Unit,
    onNavigateBack: () -> Unit,
    onDeleteRecentSearch: (String) -> Unit, // VM 통해 처리
    onDeleteAllRecentSearches: () -> Unit   // VM 통해 처리
) {
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val performSearchAction = {
        if (query.isNotBlank()) {
            focusManager.clearFocus()
            onPerformSearch(query)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            RoutineSearchTopAppBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { performSearchAction() },
                onNavigateBack = onNavigateBack,
                placeholderText = "루틴명을 검색해보세요"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TagSearchBar(
                text = "태그를 검색해 보세요.",
                onClick = onNavigateToTagSearch
            )
            Spacer(modifier = Modifier.height(24.dp))

            RecentSearchesHeader(onDeleteAll = onDeleteAllRecentSearches)

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(recentSearches) { searchText ->
                    RecentSearchItem(
                        searchText = searchText,
                        date = "07.19.",
                        onItemClick = { onPerformSearch(searchText) },
                        onDeleteClick = { onDeleteRecentSearch(searchText) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutineSearchInitialScreenPreview() {
    MORUTheme {
        RoutineSearchInitialScreen(
            // ✅ 프리뷰용 더미 데이터를 List로 전달합니다.
            recentSearches = listOf("아침 요가", "산책", "TIL 작성하기"),
            onPerformSearch = {},
            onNavigateToTagSearch = {},
            onNavigateBack = {},
            onDeleteRecentSearch = {},
            onDeleteAllRecentSearches = {}
        )
    }
}