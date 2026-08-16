package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun DotWithBentArrowByPx(
    startOffset: Offset,
    verticalLength: Float,
    horizontalLength: Float
) {
    val dotRadius = 3.dp
    val strokeWidth = 1.dp

    Canvas(
        modifier = Modifier
            .offset { IntOffset(startOffset.x.toInt(), startOffset.y.toInt()) }
            .width(with(LocalDensity.current) { (horizontalLength).toDp() + 4.dp })
            .height(with(LocalDensity.current) { (verticalLength).toDp() + 4.dp })
    ) {
        val dotRadiusPx = dotRadius.toPx()
        val strokeWidthPx = strokeWidth.toPx()

        drawCircle(Color.White, dotRadiusPx, Offset(0.5f, 0f))

        // 수직선
        drawLine(
            Color.White,
            Offset(0.5f, dotRadiusPx),
            Offset(0.5f, verticalLength),
            strokeWidthPx,
            StrokeCap.Round,
            PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
        )
        // 수평선
        drawLine(
            Color.White,
            Offset(0.5f, verticalLength),
            Offset(horizontalLength, verticalLength),
            strokeWidthPx,
            StrokeCap.Round,
            PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
        )
    }
}
