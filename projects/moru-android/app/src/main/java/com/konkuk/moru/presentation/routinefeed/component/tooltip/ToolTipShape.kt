package com.konkuk.moru.presentation.routinefeed.component.tooltip

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class TooltipShape(
    private val cornerRadius: Dp = 8.dp,
    private val tailWidth: Dp = 20.10703.dp,
    private val tailHeight: Dp = 11.56811.dp,
    private val tailHorizontalOffsetFromEnd: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val tailWidthPx = with(density) { tailWidth.toPx() }
        val tailHeightPx = with(density) { tailHeight.toPx() }
        val tailHorizontalOffsetFromEndPx = with(density) { tailHorizontalOffsetFromEnd.toPx() }

        val path = Path().apply {
            // --- 여기가 수정된 부분 ---

            // 1. 둥근 모서리를 적용할 사각형(Rect)의 좌표를 정의합니다.
            val rect = Rect(
                left = 0f,
                top = tailHeightPx,
                right = size.width,
                bottom = size.height
            )

            // 2. 위에서 정의한 Rect와 모서리 값(CornerRadius)으로 RoundRect 객체를 생성합니다.
            val roundRect = RoundRect(
                rect = rect,
                cornerRadius = CornerRadius(cornerRadiusPx)
            )

            // 3. 생성한 RoundRect 객체를 Path에 추가합니다.
            addRoundRect(roundRect)

// 꼬리(삼각형)의 중앙 x 좌표
            val tailCenterX = size.width - tailHorizontalOffsetFromEndPx

            // 꼬리 부분 그리기는 동일합니다.
            moveTo(x = tailCenterX - tailWidthPx / 2, y = tailHeightPx)
            lineTo(x = tailCenterX, y = 0f)
            lineTo(x = tailCenterX + tailWidthPx / 2, y = tailHeightPx)

            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
@Preview
fun TooltipShapeSCreen(){
    TooltipShape(tailHorizontalOffsetFromEnd=38.dp)
}