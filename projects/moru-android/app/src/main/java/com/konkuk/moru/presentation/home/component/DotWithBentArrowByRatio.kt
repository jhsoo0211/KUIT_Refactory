package com.konkuk.moru.presentation.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun DotWithBentArrowByRatio(
    startOffsetRatio: Pair<Float, Float>,
    verticalLengthRatio: Float,
    horizontalLengthRatio: Float
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp

    val startX = screenWidth * startOffsetRatio.first
    val startY = screenHeight * startOffsetRatio.second
    val verticalLength = screenHeight * verticalLengthRatio
    val horizontalLength = screenWidth * horizontalLengthRatio

    DotWithBentArrow(
        startX = startX,
        startY = startY,
        verticalLength = verticalLength,
        horizontalLength = horizontalLength
    )
}
