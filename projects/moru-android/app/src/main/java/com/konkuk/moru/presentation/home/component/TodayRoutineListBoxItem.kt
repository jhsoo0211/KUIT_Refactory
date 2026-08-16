package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TodayRoutineListBoxItem(
    routine: Routine,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .width(330.dp)
            .height(120.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(12.dp),
            )
            .background(
                color = if (routine.isRunning) Color(0xFFE8F5E8) else Color.White,  // 진행중인 루틴은 연한 초록색 배경
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (routine.isRunning) 2.dp else 0.dp,
                color = if (routine.isRunning) colors.limeGreen else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            // 1.카드 박스
            Box(
                modifier = Modifier
                    .width(298.dp)
                    .height(72.dp)
            ) {
                Row {
                    // 루틴 이미지
                    Image(
                        painter = painterResource(id = R.drawable.routine_image),
                        contentDescription = "ImageBox",
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 7.dp, bottom = 7.dp)
                    ) {
                        // 제목(ex)아침 운동)
                        Text(
                            text = routine.title,
                            style = typography.body_SB_16,
                            color = colors.black,
                        )
                        Spacer(modifier = modifier.height(2.dp))
                        // 해시태그(ex)#모닝 루틴,#스트레칭)
                        Text(
                            text = routine.tags.joinToString(" ") { "#$it" },
                            style = typography.title_B_12,
                            color = Color(0xFF8E8E8E)
                        )
                        Spacer(modifier = modifier.height(7.dp))
                        //하트와 하트 클릭 수
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.empty_heart),
                                contentDescription = "empty heart Icon",
                                modifier = Modifier.size(width = 13.33.dp, height = 11.47.dp)
                            )
                            Spacer(modifier = Modifier.width(2.67.dp))
                            Text(
                                text = "${routine.likes}",
                                style = typography.title_B_12,
                                color = colors.black
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            // 2. 요일과 시간
            Row() {
                Text(
                    text = routine.scheduledDays
                        .sortedBy { it.value } // DayOfWeek.value로 정렬 (월=1, 화=2, ..., 일=7)
                        .joinToString("") {
                            it.getDisplayName(
                                TextStyle.SHORT,
                                Locale.KOREAN
                            )
                        },
                    style = typography.title_B_12,
                    color = Color(0xFF61646B)
                )
                Spacer(modifier = Modifier.width(10.dp))

                // 설정 시간대~종료 시간대 표시
                val timeText = if (routine.scheduledTime != null) {
                    val startTime = routine.scheduledTime
                    val endTime = if (routine.requiredTime.isNotBlank()) {
                        try {
                            val durationMinutes = when {
                                routine.requiredTime.startsWith("PT") -> {
                                    val timePart = routine.requiredTime.substring(2)
                                    when {
                                        timePart.endsWith("H") -> {
                                            val hours = timePart.removeSuffix("H").toIntOrNull() ?: 0
                                            hours * 60
                                        }
                                        timePart.endsWith("M") -> {
                                            timePart.removeSuffix("M").toIntOrNull() ?: 0
                                        }
                                        else -> {
                                            var totalMinutes = 0
                                            var currentNumber = ""
                                            for (char in timePart) {
                                                when (char) {
                                                    'H' -> {
                                                        totalMinutes += (currentNumber.toIntOrNull() ?: 0) * 60
                                                        currentNumber = ""
                                                    }
                                                    'M' -> {
                                                        totalMinutes += currentNumber.toIntOrNull() ?: 0
                                                        currentNumber = ""
                                                    }
                                                    else -> currentNumber += char
                                                }
                                            }
                                            totalMinutes
                                        }
                                    }
                                }
                                else -> {
                                    val parts = routine.requiredTime.split(":")
                                    val minutes = parts.getOrNull(0)?.toIntOrNull() ?: 0
                                    val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
                                    minutes + (seconds / 60)
                                }
                            }
                            startTime.plusMinutes(durationMinutes.toLong())
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        null
                    }

                    if (endTime != null) {
                        "${startTime.format(DateTimeFormatter.ofPattern("HH:mm"))}~${endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                    } else {
                        startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                    }
                } else {
                    "시간 미설정"
                }

                Text(
                    text = timeText,
                    style = typography.title_B_12,
                    color = Color(0xFF61646B)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 330,
    heightDp = 120
)
@Composable
private fun TodayRoutineListBoxItemPreview() {
    val sampleRoutine = Routine(
        routineId = "routine-1",
        title = "주말 아침 루틴",
        description = "설명",
        imageUrl = null,
        category = "건강",
        tags = listOf("#모닝", "#운동"),
        authorId = "user-1",
        authorName = "홍길동",
        authorProfileUrl = null,
        likes = 42,
        isLiked = false,
        isBookmarked = false,
        isRunning = false,
        scheduledTime = LocalTime.of(9, 0),
        scheduledDays = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
        isAlarmEnabled = true,
        steps = emptyList(),
        similarRoutines = emptyList(),
        usedApps = emptyList()
    )

    TodayRoutineListBoxItem(
        routine = sampleRoutine,
        onClick = {}
    )
}
