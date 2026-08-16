package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.konkuk.moru.core.util.toStableIntId
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.ui.theme.MORUTheme.colors
import java.time.DayOfWeek
import java.time.LocalTime

// 2. Pager 컴포저블
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TodayRoutinePager(
    routines: List<Routine>,
    onRoutineClick: (Routine, Int) -> Unit
) {
    //현재 페이지가 몇 페이지인지 기억하기
    val pagerState = rememberPagerState(initialPage = 0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(184.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        HorizontalPager(
            count = routines.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) { page ->
            val routine = routines[page]
            TodayRoutineListBoxItem(
                routine = routine,
                onClick = { onRoutineClick(routine, page) }
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(4.dp),
            activeColor = colors.black,
            inactiveColor = colors.lightGray
        )
        Spacer(modifier = Modifier.height(18.dp))
    }

}

@Preview(
    showBackground = true,
    widthDp = 360,
)
@Composable
private fun TodayRoutinePagerPreview() {
    val sampleRoutines = listOf(
        Routine(
            routineId = "routine-1",
            title = "주말 아침 루틴 (집중 루틴 테스트용)",
            description = "설명",
            imageUrl = null,
            category = "생활",
            tags = listOf("#화이팅"),
            authorId = "user-1",
            authorName = "홍길동",
            authorProfileUrl = null,
            likes = 25,
            isLiked = false,
            isBookmarked = false,
            isRunning = false,
            scheduledTime = LocalTime.of(9, 0),
            scheduledDays = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        ),
        Routine(
            routineId = "routine-1",
            title = "출근 준비 루틴 (간편 루틴 테스트용)",
            description = "설명",
            imageUrl = null,
            category = "생활",
            tags = listOf("#힘내자"),
            authorId = "user-101",
            authorName = "김영희",
            authorProfileUrl = null,
            likes = 41,
            isLiked = true,
            isBookmarked = true,
            isRunning = true,
            scheduledTime = LocalTime.of(8, 0),
            scheduledDays = setOf(DayOfWeek.MONDAY)
        ),
        Routine(
            routineId = "routine-3",
            title = "운동 루틴",
            description = "설명",
            imageUrl = null,
            category = "운동",
            tags = listOf("#건강"),
            authorId = "user-102",
            authorName = "이철수",
            authorProfileUrl = null,
            likes = 12,
            isLiked = false,
            isBookmarked = false,
            isRunning = false,
            scheduledTime = LocalTime.of(18, 0),
            scheduledDays = setOf(DayOfWeek.WEDNESDAY)
        )
    )

    TodayRoutinePager(
        routines = sampleRoutines,
        onRoutineClick = { _, _ -> }
    )
}


