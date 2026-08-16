package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.util.toStableIntId
import com.konkuk.moru.data.model.Routine
import java.time.DayOfWeek

@Composable
fun RoutineCardList(
    routines: List<Routine>,
    onRoutineClick: (String) -> Unit,
    runningHighlightIds: List<Int> = emptyList(),
    modifier: Modifier = Modifier
) {
    //스크롤 대비 상태 저장
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        routines.forEach { routine ->
            val stableId = routine.routineId.toStableIntId()
            val isHighlighted = runningHighlightIds.contains(stableId)

            RoutineCardItem(
                title = routine.title,
                tags = routine.tags,
                scheduledDays = routine.scheduledDays,
                onClick = { onRoutineClick(routine.routineId) },
                isHighlighted = isHighlighted
            )
        }

    }
}

@Preview
@Composable
private fun RoutineCardListPreview() {
    val dummyRoutines = listOf(
        Routine(
            routineId = "routine-1",
            title = "MORU의 집중 코딩",
            description = "집중력을 높이는 코딩 루틴",
            imageUrl = null,
            category = "공부",
            tags = listOf("개발", "코딩"),
            authorId = "user-1",
            authorName = "MORU",
            authorProfileUrl = null,
            likes = 10,
            isLiked = false,
            isBookmarked = false,
            isRunning = false,
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        ),
        Routine(
            routineId = "routine-2",
            title = "아침 명상 루틴",
            description = "마음을 가다듬는 아침 명상",
            imageUrl = null,
            category = "건강",
            tags = listOf("명상", "아침"),
            authorId = "user-2",
            authorName = "MORU",
            authorProfileUrl = null,
            likes = 8,
            isLiked = true,
            isBookmarked = false,
            isRunning = false,
            scheduledDays = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY)
        ),
        Routine(
            routineId = "routine-3",
            title = "주말 대청소",
            description = "집을 깨끗이! 대청소 루틴",
            imageUrl = null,
            category = "생활",
            tags = listOf("청소", "집안일"),
            authorId = "user-3",
            authorName = "MORU",
            authorProfileUrl = null,
            likes = 5,
            isLiked = false,
            isBookmarked = true,
            isRunning = false,
            scheduledDays = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        )
    )

    RoutineCardList(
        routines = dummyRoutines,
        onRoutineClick = {},
        runningHighlightIds = listOf("routine-2".toStableIntId())
    )
}
