package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun WeeklyCalendarView(
    modifier: Modifier = Modifier,
    routinesPerDate: Map<Int, List<String>>, // 날짜별 루틴 목록
    today: Int // ⬅️ 오늘 날짜 (예: 10)
) {
    val daysOfWeek = listOf("월", "화", "수", "목", "금", "토", "일")

    // 이번주(월~일) 날짜 계산 (월 기준)
    val startOfWeek = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    Column(
        modifier = modifier
            .wrapContentHeight()
            .heightIn(min = 180.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // 요일 행
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            daysOfWeek.forEach { day ->
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = day, style = typography.title_B_14, color = colors.black)
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // 날짜 행
        Row(Modifier.fillMaxWidth()) {
            weekDates.forEach { date ->
                val dom = date.dayOfMonth
                val isToday = dom == today
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        // 날짜 표시 (오늘 하이라이트)
                        if (isToday) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(colors.limeGreen, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$dom",
                                    style = typography.title_B_14,
                                    color = Color.White
                                )
                            }
                        } else {
                            Text(
                                text = "$dom",
                                style = typography.title_B_14,
                                color = colors.mediumGray
                            )
                        }

                        // 루틴 태그들
                        Box(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(top = 4.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                routinesPerDate[dom]?.forEachIndexed { index, label ->
                                    if (index != 0) Spacer(Modifier.height(2.dp))
                                    RoutineTag(label)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 태그 컴포넌트
@Composable
fun RoutineTag(
    text: String,
    minWidth: Dp = 60.dp
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val tagWidth = screenWidth / 7.5f

    Text(
        text = text,
        fontSize = 8.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 9.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        softWrap = true,
        maxLines = 5,
        modifier = Modifier
            .width(tagWidth)
            .background(
                color = colors.darkGray,
                shape = RoundedCornerShape(2.dp)
            )
            .defaultMinSize(minWidth = minWidth)
            .padding(horizontal = 4.dp, vertical = 3.dp)
    )
}


@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 200
)
@Composable
private fun WeeklyCalendarViewPreview() {
    val sampleRoutines = mapOf(
        8 to listOf("아침 운동", "회의"),
        10 to listOf("아침 운동"),
        12 to listOf("아침 운동", "회의"),
        13 to listOf("주말아침완전 집중루틴"),
        14 to listOf("주말아침루틴")
    )

    WeeklyCalendarView(
        routinesPerDate = sampleRoutines,
        today = 13 // 오늘 날짜 강조
    )
}
