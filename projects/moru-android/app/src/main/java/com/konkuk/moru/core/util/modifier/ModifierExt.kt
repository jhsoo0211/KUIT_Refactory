package com.konkuk.moru.core.util.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

fun Modifier.softShadow(
    color: Color = Color.Black,
    alpha: Float = 0.12f,
    shadowRadius: Dp = 16.dp,
    cornerRadius: Dp = 16.dp,
): Modifier = this.drawBehind {
    val shadowColor = color.copy(alpha = alpha)
    val paint = Paint().asFrameworkPaint().apply {
        this.color = shadowColor.toArgb()
        this.setShadowLayer(shadowRadius.toPx(), 0f, 0f, this.color)
    }

    drawContext.canvas.nativeCanvas.apply {
        save()
        drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            cornerRadius.toPx(),
            cornerRadius.toPx(),
            paint
        )
        restore()
    }
}
