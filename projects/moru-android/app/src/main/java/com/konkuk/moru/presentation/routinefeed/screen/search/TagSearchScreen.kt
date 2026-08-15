package com.konkuk.moru.presentation.routinefeed.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.moru.data.model.TagItem
import com.konkuk.moru.presentation.routinefeed.component.search.BackTitle
import com.konkuk.moru.presentation.routinefeed.component.search.HashTagSearchField
import com.konkuk.moru.presentation.routinefeed.component.search.TagChip
import com.konkuk.moru.presentation.routinefeed.component.search.TagSectionHeader
import com.konkuk.moru.presentation.routinefeed.viewmodel.SearchViewModel
import com.konkuk.moru.ui.theme.MORUTheme
//@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagSearchScreen(
    originalQuery: String,
    onNavigateBack: () -> Unit,
    onTagSelected: (String) -> Unit
) {
    val vm: SearchViewModel = hiltViewModel()
    val ui by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (ui.allTags.isEmpty()) vm.refreshAllTags()
        if (ui.favoriteTags.isEmpty()) vm.refreshFavoriteTags()
    }

    var query by remember { mutableStateOf("") }
    val isHashtagMode = query.trim().startsWith("#")

    // 변경: 관심 태그는 서버에서 가져온 목록 사용
    val favoriteTags = ui.favoriteTags    // 관심태그 (서버)
    val allTags: List<TagItem> = ui.allTags

    // 변경: 전체태그 대용 - 관심태그에서 필터
    val filteredTags = remember(query, allTags) {
        val q = query.removePrefix("#")
        if (q.isBlank()) allTags else allTags.filter { it.name.contains(q, ignoreCase = true) }
    }

    val titleText = if (originalQuery.isNotBlank()) originalQuery else "내 기록"

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(14.dp))
                    BackTitle(title = titleText, onNavigateBack = onNavigateBack)
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MORUTheme.colors.lightGray)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                HashTagSearchField(
                    value = query,
                    onValueChange = { query = it },
                    onSearch = {
                        if (query.isNotBlank()) {
                            onTagSelected(query)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(25.dp))

                TagSectionHeader("관심태그")
                Spacer(modifier = Modifier.height(24.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    favoriteTags.forEach { tag ->
                        TagChip(
                            text = "#${tag.name.removePrefix("#")}",
                            selected = false,
                            showCloseIcon = false, // 변경: 검색 선택용이므로 닫기 X
                            onClick = { onTagSelected(tag.name) } // VM이 '#' 보정
                        )
                    }
                }

                Spacer(modifier = Modifier.height(42.dp))
                TagSectionHeader("전체태그")
                Spacer(modifier = Modifier.height(24.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    filteredTags.forEach { tag ->
                        TagChip(
                            text = "#${tag.name.removePrefix("#")}", // 표시용 '#'
                            selected = false,
                            onClick = { onTagSelected(tag.name) }
                        )
                    }
                }
            }
        }

        // '#' 입력 시 나타나는 검색 오버레이는 그대로 유지됩니다.
        if (isHashtagMode) {
            HashtagSearchResultOverlay(
                query = query,
                onQueryChange = { query = it },
                tags = filteredTags, // TagItem
                onTagClick = { tag -> onTagSelected(tag.name) },
                onDismiss = { query = "" }
            )
        }

    }
}

/**
 * '#' 검색 시 나타나는 오버레이 UI
 */
@Composable
private fun HashtagSearchResultOverlay(
    query: String,
    onQueryChange: (String) -> Unit,
    tags: List<TagItem>,                 // [CHANGE] TagItem으로 교체
    onTagClick: (TagItem) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                .clickable(enabled = false) {}
        ) {
            HashTagSearchField(query, onValueChange = onQueryChange)
            Spacer(modifier = Modifier.height(17.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.65.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                tags.forEach { tag ->
                    HashtagResultItem(
                        tag = tag,
                        onClick = { onTagClick(tag) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

/**
 * 오버레이에 표시되는 개별 태그 아이템
 */
@Composable
private fun HashtagResultItem(
    tag: TagItem,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(Color.Black)
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp)
    ) {
        Text(
            text = "#${tag.name.removePrefix("#")}",
            color = MORUTheme.colors.limeGreen,
            style = MORUTheme.typography.time_R_14
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TagSearchScreenPreview() {
    MORUTheme {
        TagSearchScreen(
            onNavigateBack = {},
            originalQuery = "", onTagSelected = {})
    }
}