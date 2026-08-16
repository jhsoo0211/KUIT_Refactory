package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.konkuk.moru.presentation.home.screen.HomeTutorialOverlayView
import com.konkuk.moru.presentation.home.screen.TutorialOverlayView

@Composable
fun HomeTutorialOverlayContainer(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onFabClick: () -> Unit,
    fabOffsetY: Float,
    todayTabOffsetY: Float,
    bottomIconCenters: List<Offset>,
    onCreateImmediatelyClick: () -> Unit = onFabClick
) {
    LaunchedEffect(bottomIconCenters) {
        bottomIconCenters.forEachIndexed { idx, offset ->
        }
    }

    val density = LocalDensity.current
    val config = LocalConfiguration.current

    val rectHole = remember(todayTabOffsetY) { // todayTabOffsetY 값이 변경될 때마다 재계산
        with(density) {
            val tabHeightPx = 36.dp.toPx()

            val horizontalPaddingPx = 16.dp.toPx() // 16dp를 픽셀로 변환

            // 화면 너비
            val screenWidthPx = config.screenWidthDp.dp.toPx()

            // 구멍의 위치 계산
            val holeLeft = horizontalPaddingPx
            val holeRight = screenWidthPx - horizontalPaddingPx
            val holeTop = todayTabOffsetY - (tabHeightPx / 2f)
            val holeBottom = todayTabOffsetY + (tabHeightPx / 2f)

            TutorialOverlayView.HolePx(
                left = holeLeft,
                top = holeTop,
                right = holeRight,
                bottom = holeBottom,
                isCircle = false
            )
        }
    }

    val circleHole = remember(fabOffsetY) {
        with(density) {
            val fabSizePx = 63.dp.toPx()
            val fabPaddingEndPx = 16.dp.toPx()

            val screenWidthPx = config.screenWidthDp.dp.toPx()

            // FAB의 중심 좌표 계산
            val fabCenterX = screenWidthPx - fabPaddingEndPx - fabSizePx / 2f
            val fabCenterY = fabOffsetY

            // 구멍의 위치 계산
            val holeLeft = fabCenterX - fabSizePx / 2f
            val holeTop = fabCenterY - fabSizePx / 2f
            val holeRight = fabCenterX + fabSizePx / 2f
            val holeBottom = fabCenterY + fabSizePx / 2f

            TutorialOverlayView.HolePx(
                left = holeLeft,
                top = holeTop,
                right = holeRight,
                bottom = holeBottom,
                isCircle = true
            )
        }
    }

    val holes = remember(rectHole, circleHole) { listOf(rectHole, circleHole) }

    Box(modifier = modifier.fillMaxSize()) {
        HomeTutorialOverlayView(
            holes = holes,
        )

        HomeTutorialDecoration(
            onDismiss = onDismiss,
            onFabClick = onFabClick,
            bottomIconCenters = bottomIconCenters,
            todayTabOffsetY = todayTabOffsetY,
            fabOffsetY = fabOffsetY,
            onCreateImmediatelyClick = onCreateImmediatelyClick
        )

        BottomOverlayBar(
            iconCenters = bottomIconCenters,
            modifier = Modifier.zIndex(3f)
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun HomeTutorialOverlayContainerPreview() {
    HomeTutorialOverlayContainer(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f),
        onDismiss = {},
        onFabClick = {},
        fabOffsetY = 632.5f,
        todayTabOffsetY = 283f,
        bottomIconCenters = listOf(
            Offset.Zero,
            Offset(134f, 748f),
            Offset(226f, 748f),
            Offset(318f, 748f)
        ),
        onCreateImmediatelyClick = {}
    )
}
