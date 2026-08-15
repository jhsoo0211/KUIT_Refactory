package com.konkuk.moru.presentation.routinefeed.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.moru.R
import com.konkuk.moru.domain.repository.SortType
import com.konkuk.moru.presentation.routinefeed.component.search.RoutineSearchTopAppBar
import com.konkuk.moru.presentation.routinefeed.component.search.SelectedTagChip
import com.konkuk.moru.presentation.routinefeed.component.search.SortButtons
import com.konkuk.moru.presentation.routinefeed.viewmodel.SearchViewModel
import com.konkuk.moru.ui.theme.MORUTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RoutineNameResultScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onPerformSearch: () -> Unit, // TopAppBar에서 검색 실행을 위한 콜백
    selectedTags: List<String>,
    onNavigateBack: () -> Unit,
    onRoutineClick: (String) -> Unit,
    onNavigateToTagSearch: () -> Unit,
    onDeleteTag: (String) -> Unit
) {


    // 변경: ViewModel 상태로 결과/정렬 제어
    val vm: SearchViewModel = hiltViewModel()
    val ui by vm.uiState.collectAsState()

    // 변경: sortOption -> VM의 sortType과 동기
    val sortOption = when (ui.sortType) {
        SortType.LATEST -> "최신순"
        SortType.POPULAR -> "인기순"
    }



    Scaffold(
        containerColor = Color.White,
        topBar = {
            // 1️⃣. TopAppBar를 루틴명 검색 전용 컴포넌트로 교체
            RoutineSearchTopAppBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onPerformSearch,
                onNavigateBack = onNavigateBack,
                placeholderText = "루틴명을 검색해보세요"
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 2️⃣. 태그 검색 및 표시 영역: 스크린샷과 동일한 UI로 교체
            item {
                SelectedTagsSection(
                    modifier = Modifier.padding(16.dp),
                    selectedTags = ui.selectedTags,                 // ← 변경
                    onDeleteTag = { tag ->
                        vm.removeTag(tag)                           // ← 변경: VM에 반영
                        vm.performSearch(resetPage = true)          // ← 변경: 즉시 재검색
                    },
                    onNavigateToTagSearch = onNavigateToTagSearch
                )
            }

            // 3️⃣. 정렬 버튼
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    SortButtons(
                        selectedOption = sortOption,
                        onOptionSelected = { text ->
                            // 변경: 정렬 변경 -> 서버 sortType으로 매핑
                            val newSort = if (text == "인기순") SortType.POPULAR else SortType.LATEST
                            vm.setSort(newSort)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 4️⃣. 검색 결과 목록
            items(ui.results) { routine ->
                com.konkuk.moru.core.component.routine.RoutineListItem(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    isRunning = routine.isRunning,
                    routineName = routine.title,
                    tags = routine.tags,
                    likeCount = routine.likeCount,
                    isLiked = routine.isLiked,
                    onItemClick = { onRoutineClick(routine.routineId) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // (옵션) 페이징 로드 모어 트리거
            if (ui.canLoadMore) {
                item {
                    androidx.compose.runtime.LaunchedEffect(ui.page) {
                        vm.loadMore() // 스크롤 끝에서 자동 호출하려면 LazyColumn state와 연동해도 좋음
                    }
                }
            }
        }
    }
}

/**
 * 스크린샷의 UI와 동일하게, 선택된 태그와 '+' 버튼을 함께 보여주는 컴포넌트
 */
@Composable
private fun SelectedTagsSection(
    modifier: Modifier = Modifier,
    selectedTags: List<String>,
    onDeleteTag: (String) -> Unit,
    onNavigateToTagSearch: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp) // 내부 컨텐츠 크기에 맞게 높이 조절
            .clip(RoundedCornerShape(100.dp))
            .background(MORUTheme.colors.veryLightGray)
            .border(1.dp, MORUTheme.colors.lightGray, RoundedCornerShape(100.dp))
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 선택된 태그 칩들을 보여주는 LazyRow
        LazyRow(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(selectedTags) { tag ->
                SelectedTagChip(text = tag) { onDeleteTag(tag) }
            }
        }

        // '+' 아이콘 버튼
        IconButton(
            onClick = onNavigateToTagSearch,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus_search), // ◀ 이미지 리소스로 교체
                contentDescription = "태그 추가 검색",
                tint = Color.Unspecified
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoutineNameResultScreenPreview() {
    MORUTheme {
        RoutineNameResultScreen(
            query = "아침 운동",
            onQueryChange = {},
            onPerformSearch = {},
            selectedTags = listOf("#태그그그태", "#태그2", "#tag"),
            onNavigateBack = {},
            onRoutineClick = { },
            onNavigateToTagSearch = {},
            onDeleteTag = {}
        )
    }
}