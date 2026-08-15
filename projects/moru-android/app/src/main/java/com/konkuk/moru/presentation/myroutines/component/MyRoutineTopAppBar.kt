package com.konkuk.moru.presentation.myroutines.component

/**
 * '내 루틴' 화면의 TopAppBar와 요일 선택 탭
 * (상태 호이스팅이 적용되어 재사용 및 관리가 용이한 버전)
 *
 * @param onInfoClick 정보 아이콘 클릭 시 동작
 * @param onTrashClick 휴지통 아이콘 클릭 시 동작
 * @param onDaySelected 요일 탭 선택 시 동작 (선택된 DayOfWeek 전달, 해제 시 null 전달)
 * @param selectedDay 외부에서 전달받는 현재 선택된 요일 (State)
 */
// [추가]
// [추가]

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.moruFontRegular
import com.konkuk.moru.ui.theme.moruFontSemiBold
import java.time.DayOfWeek
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRoutineTopAppBar(
    onInfoClick: () -> Unit,
    onTrashClick: () -> Unit,
    onDaySelected: (DayOfWeek?) -> Unit,
    selectedDay: DayOfWeek?,
    modifier: Modifier = Modifier
) {
    val days = DayOfWeek.values()
    val tabHeight = 33.dp
    val topDividerThickness = 1.dp      // 상단 회색 베이스 라인
    val indicatorThickness = 1.dp       // 흰색 인디케이터 두께
    val indicatorHorizontalInset = 5.dp // 흰 줄 좌우 인셋(원하는 간격으로 조절)

    Column(modifier = modifier.background(MORUTheme.colors.charcoalBlack)) {
        TopAppBar(

            title = {
                Text(
                    text = "내 루틴",
                    fontSize = 16.sp,
                    color = MORUTheme.colors.limeGreen,
                    fontFamily = moruFontSemiBold,
                    fontWeight = FontWeight(600)
                )
            },
            actions = {
                IconButton(onClick = onInfoClick) {
                    Icon(
                        painterResource(R.drawable.ic_info),
                        contentDescription = "정보",
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onTrashClick) {
                    Icon(
                        painterResource(R.drawable.ic_trash),
                        contentDescription = "삭제",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                actionIconContentColor = Color.Gray
            )
        )

        val tabShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 4.dp, bottomEnd = 4.dp)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(tabShape),
            shape = tabShape,
            color = MORUTheme.colors.charcoalBlack,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column {
                // 🔹 스샷처럼 탭 “위”에 얇은 회색 선 (양끝까지 풀폭)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topDividerThickness)
                        .background(MORUTheme.colors.darkGray)
                )

                TabRow(
                    modifier = Modifier.height(tabHeight),
                    selectedTabIndex = selectedDay?.value?.minus(1) ?: 0,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    divider = {}, // 기본 하단선 제거
                    // 🔸 인디케이터를 "상단"에, 좌우 인셋을 주고 그리기
                    indicator = { tabPositions ->
                        selectedDay?.let { day ->
                            val index = day.value - 1
                            if (index in tabPositions.indices) {
                                val pos = tabPositions[index]
                                if (pos.width > indicatorHorizontalInset * 2) {
                                    Box(Modifier.fillMaxWidth()) {
                                        Box(
                                            Modifier
                                                .offset(x = pos.left + indicatorHorizontalInset, y = 0.dp)
                                                .width(pos.width - indicatorHorizontalInset * 2)
                                                .height(indicatorThickness)
                                                .align(Alignment.TopStart)
                                                .background(Color.White)
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) {
                    days.forEach { day ->
                        val isSelected = selectedDay == day
                        Tab(
                            selected = isSelected,
                            onClick = {
                                val newSelection = if (isSelected) null else day
                                onDaySelected(newSelection)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(tabHeight),
                            selectedContentColor = Color.White,
                            unselectedContentColor = MORUTheme.colors.darkGray
                        ) {
                            // 가운데 정렬 텍스트 (잘림 방지)
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = day.name.take(3)
                                        .lowercase()
                                        .replaceFirstChar { it.titlecase(Locale.getDefault()) },
                                    fontFamily = moruFontRegular,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoruTab(
    label: String,
    selected: Boolean,
    height: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier.height(height),
        selectedContentColor = Color.White,
        unselectedContentColor = MORUTheme.colors.darkGray
    ) {
        // 정중앙 배치로 글씨 잘림 방지 + 고정 높이 내에서 중앙 정렬
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontFamily = moruFontRegular,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Clip // 3글자라 ellipsis 불필요
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyRoutineTopAppBarPreview() {
    var selectedDay by remember { mutableStateOf<DayOfWeek?>(DayOfWeek.MONDAY) }
    MaterialTheme {
        MyRoutineTopAppBar(
            onInfoClick = {},
            onTrashClick = {},
            selectedDay = selectedDay,
            onDaySelected = { day -> selectedDay = day }
        )
    }
}