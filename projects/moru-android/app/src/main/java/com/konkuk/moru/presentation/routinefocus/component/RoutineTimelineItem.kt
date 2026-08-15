package com.konkuk.moru.presentation.routinefocus.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
fun RoutineTimelineItem(
    time: String, //소요시간
    title: String, //세부 루틴명
    index: Int, // 1부터 시작
    currentStep: Int, // 1부터 시작
    isTimeout: Boolean, // 시간 초과 유무
    isDarkMode: Boolean, // 다크 모드 유무
    onStepClick: ((Int) -> Unit)? = null // 스텝 클릭 콜백 (옵셔널)
) {
    val colors = MORUTheme.colors
    val typography = MORUTheme.typography

    // 진행했거나 진행 중이거나
    val isActive = index <= currentStep

    // 진행상태에 따른 텍스트
    val textColor = when {
        // 화이트 모드
        !isDarkMode && isActive && !isTimeout -> colors.black
        !isDarkMode && !isActive && !isTimeout -> colors.darkGray
        !isDarkMode && isActive && isTimeout -> colors.black
        !isDarkMode && !isActive && isTimeout -> colors.darkGray

        // 다크 모드
        isDarkMode && isActive && !isTimeout -> colors.veryLightGray
        isDarkMode && !isActive && !isTimeout -> colors.darkGray
        isDarkMode && isActive && isTimeout -> Color.White
        isDarkMode && !isActive && isTimeout -> colors.darkGray

        else -> colors.black // fallback
    }
    val lineColor = when {
        // 화이트 모드
        !isDarkMode && isActive && !isTimeout -> colors.limeGreen
        !isDarkMode && !isActive && !isTimeout -> colors.lightGray
        !isDarkMode && isActive && isTimeout -> colors.black
        !isDarkMode && !isActive && isTimeout -> colors.lightGray

        // 다크 모드
        isDarkMode && isActive && !isTimeout -> colors.limeGreen
        isDarkMode && !isActive && !isTimeout -> colors.mediumGray
        isDarkMode && isActive && isTimeout -> Color.White
        isDarkMode && !isActive && isTimeout -> colors.mediumGray

        else -> colors.lightGray  // fallback (혹시 몰라서)
    }

    // 점선의 촘촘한 정도
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(7f, 7f))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = if (onStepClick != null) {
            Modifier.clickable { onStepClick(index) }
        } else {
            Modifier
        }
    ) {
        // 시간
        Text(
            text = time,
            style = typography.time_R_14,
            color = textColor,
            modifier = Modifier.width(40.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        // 타임라인
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2

                // 위 선
                drawLine(
                    color = lineColor,
                    start = Offset(centerX, 0f),
                    end = Offset(centerX, centerY - 6.dp.toPx()),
                    strokeWidth = 3.dp.toPx(),
                    pathEffect = if (isActive) null else pathEffect
                )

                // 아래 선
                drawLine(
                    color = lineColor,
                    start = Offset(centerX, centerY + 6.dp.toPx()),
                    end = Offset(centerX, size.height),
                    strokeWidth = 3.dp.toPx(),
                    pathEffect = if (isActive) null else pathEffect
                )

                if (isActive) {
                    //원 내부 칠하기
                    drawCircle(
                        color = if (isDarkMode) colors.black else Color.White,
                        center = Offset(centerX, centerY),
                        radius = 6.dp.toPx(),
                        style = Fill
                    )

                    // 원 테두리
                    drawCircle(
                        color = lineColor,
                        center = Offset(centerX, centerY),
                        radius = 6.dp.toPx(),
                        style = Stroke(width = 2.dp.toPx())
                    )
                } else {
                    // 미진행 상태: 작은 회색 원
                    drawCircle(
                        color = colors.lightGray,
                        center = Offset(centerX, centerY),
                        radius = 4.dp.toPx(),
                        style = Fill
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        // 태스크 이름
        Text(
            text = title,
            style = typography.time_R_16,
            color = textColor,
            maxLines = 2,
            softWrap = true,
            modifier = Modifier.widthIn(max = 120.dp)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun RoutineTimelineItemPreview1() {
    val routineItems = listOf(
        "샤워하기" to "15m",
        "청소하기" to "10m",
        "밥먹기" to "30m",
        "옷갈아입기" to "3m"
    )

    // isTimeout = false + 화이트 모드
    Column {
        routineItems.forEachIndexed { rawIndex, (title, time) ->
            val index = rawIndex + 1
            RoutineTimelineItem(
                time = time,
                title = title,
                index = index,
                currentStep = 2, // limeGreen 스타일은 1, 2번만 적용됨
                isTimeout = false,
                isDarkMode = false
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun RoutineTimelineItemPreview2() {
    val routineItems = listOf(
        "샤워하기" to "15m",
        "청소하기" to "10m",
        "밥먹기" to "30m",
        "옷갈아입기" to "3m"
    )

    // isTimeout = true + 화이트 모드
    Column {
        routineItems.forEachIndexed { rawIndex, (title, time) ->
            val index = rawIndex + 1
            RoutineTimelineItem(
                time = time,
                title = title,
                index = index,
                currentStep = 2,   // 중요하지 않음
                isTimeout = true,   // ← 시간 초과 상태
                isDarkMode = false
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun RoutineTimelineItemPreview3() {
    val routineItems = listOf(
        "샤워하기" to "15m",
        "청소하기" to "10m",
        "밥먹기" to "30m",
        "옷갈아입기" to "3m"
    )
    // isTimeout = false + 다크모드
    Column {
        routineItems.forEachIndexed { rawIndex, (title, time) ->
            val index = rawIndex + 1
            RoutineTimelineItem(
                time = time,
                title = title,
                index = index,
                currentStep = 2, // limeGreen 스타일은 1, 2번만 적용됨
                isTimeout = false,
                isDarkMode = true
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun RoutineTimelineItemPreview4() {
    val routineItems = listOf(
        "샤워하기" to "15m",
        "청소하기" to "10m",
        "밥먹기" to "30m",
        "옷갈아입기" to "3m"
    )

    // isTimeout = true + 다크모드
    Column {
        routineItems.forEachIndexed { rawIndex, (title, time) ->
            val index = rawIndex + 1
            RoutineTimelineItem(
                time = time,
                title = title,
                index = index,
                currentStep = 2, // limeGreen 스타일은 1, 2번만 적용됨
                isTimeout = true,
                isDarkMode = true
            )
        }
    }
}