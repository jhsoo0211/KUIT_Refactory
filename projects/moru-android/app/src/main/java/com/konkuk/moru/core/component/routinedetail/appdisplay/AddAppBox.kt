package com.konkuk.moru.core.component.routinedetail.appdisplay

import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import androidx.compose.ui.draw.drawBehind

// ✅ 점선 테두리를 그리는 Modifier
fun Modifier.drawDashedBorder(
    paintColor: Color, // ✅ 이름 변경
    strokeWidthDp: Float = 2f,
    cornerRadiusDp: Float = 12f,
    dashLengthDp: Float = 10f,
    gapLengthDp: Float = 5f
): Modifier = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = strokeWidthDp * density
        val dashLengthPx = dashLengthDp * density
        val gapLengthPx = gapLengthDp * density
        val cornerRadiusPx = cornerRadiusDp * density

        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = paintColor.toArgb()
            strokeWidth = strokeWidthPx
            pathEffect = DashPathEffect(floatArrayOf(dashLengthPx, gapLengthPx), 0f)
        }

        val rect = RectF(
            strokeWidthPx / 2,
            strokeWidthPx / 2,
            size.width - strokeWidthPx / 2,
            size.height - strokeWidthPx / 2
        )

        drawContext.canvas.nativeCanvas.drawRoundRect(
            rect,
            cornerRadiusPx,
            cornerRadiusPx,
            paint
        )
    }
)


@Composable
fun AddAppBox(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(53.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = colors.veryLightGray, shape = RoundedCornerShape(6.dp))
                .drawDashedBorder(
                    colors.darkGray,
                    strokeWidthDp = 1f,
                    cornerRadiusDp = 6f,
                    dashLengthDp = 4f,
                    gapLengthDp = 2f
                )
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = null
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_routine_add_app),
                contentDescription = "Add App",
            )
        }
    }
}

@Preview
@Composable
private fun AddAppBoxPreview() {
    AddAppBox { }
}