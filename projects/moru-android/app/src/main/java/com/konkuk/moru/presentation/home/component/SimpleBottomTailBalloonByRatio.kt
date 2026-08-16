package com.konkuk.moru.presentation.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SimpleBottomTailBalloonByRatio(
    text: String,
    image: Int,
    offsetRatioX: Float,
    offsetRatioY: Float,
    balloonWidth: Dp = 138.dp,
    balloonHeight: Dp = 42.dp,
    onClick: (() -> Unit)? = null
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp

    val offsetX = screenWidth * offsetRatioX
    val offsetY = screenHeight * offsetRatioY

    SimpleBottomTailBalloon(
        text = text,
        image = image,
        offsetX = offsetX,
        offsetY = offsetY,
        balloonWidth = balloonWidth,
        balloonHeight = balloonHeight,
        onClick = onClick
    )
}
