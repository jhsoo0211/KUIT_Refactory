package com.konkuk.moru.presentation.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


// 비율에 따른 도움말 텍스트 위치를 위한 함수
@Composable
fun TutorialHintWithLineByRatio(
    text: String,
    textOffsetRatio: Pair<Float, Float>,
    lineOffsetRatio: Pair<Float, Float>,
    lineLengthRatio: Float,
    isUpward: Boolean
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val textOffsetDp = with(density) {
        Pair(
            (screenWidthPx * textOffsetRatio.first).toDp(),
            (screenHeightPx * textOffsetRatio.second).toDp()
        )
    }

    val lineOffsetDp = with(density) {
        Pair(
            (screenWidthPx * lineOffsetRatio.first).toDp(),
            (screenHeightPx * lineOffsetRatio.second).toDp()
        )
    }

    val lineLengthDp = with(density) {
        (screenHeightPx * lineLengthRatio).toDp()
    }

    // 실제 점선과 텍스트를 그림
    TutorialHintWithLine(
        text = text,
        textOffsetDp = textOffsetDp,
        lineOffsetDp = lineOffsetDp,
        lineLength = lineLengthDp,
        isUpward = isUpward
    )
}

