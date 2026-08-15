package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R

@Composable
fun BottomOverlayBar(
    modifier: Modifier = Modifier,
    iconCenters: List<Offset>
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    Box(modifier = modifier) {
        iconCenters.forEachIndexed { index, center ->
            if (center != Offset.Zero && index in 1..3) {
                val label = with(density) {
                    when (index) {
                        1 -> if (density.density >= 3.0f) "루틴\u200A 피드" else "루틴\u2004 피드"
                        2 -> if (density.density >= 3.0f) "내\u200A 루틴" else "내\u2004 루틴"
                        3 -> if (density.density >= 3.0f) "내\u200A 활동" else "내\u2004 활동"
                        else -> ""
                    }
                }

                val iconResId = when (index) {
                    1 -> R.drawable.ic_routine_feed_white
                    2 -> R.drawable.ic_my_routine_white
                    3 -> R.drawable.ic_my_activity_white
                    else -> return@forEachIndexed
                }

                with(density) {
                    // 실제 화면 너비를 가져와서 정확히 계산
                    val screenWidthPx = configuration.screenWidthDp.dp.toPx()
                    val itemWidth = screenWidthPx / 4f // 4개 탭으로 균등 분할
                    val itemHeight = 80.dp.toPx() // 바텀바 높이

                    // 오버레이 아이템의 중심을 바텀바 아이템의 중심과 정확히 맞춤
                    val offsetX = (center.x - itemWidth / 2f).toDp()
                    val offsetY = (center.y - itemHeight / 2f).toDp()

                    // 기기별 보정값
                    val iconYCorrection =
                        if (density.density >= 3.0f) (-2).dp else (-1).dp // 고밀도 기기는 더 많이 보정
                    val textXCorrection =
                        if (density.density >= 3.0f) (-1).dp else (-2).dp // 저밀도 기기는 덜 보정

                    BottomBarIconWithLabelOverlay(
                        iconResId = iconResId,
                        label = label,
                        offsetX = offsetX,
                        offsetY = offsetY,
                        itemWidth = itemWidth.toDp(),
                        itemHeight = itemHeight.toDp(),
                        iconWidth = 16.dp,
                        iconHeight = 17.5.dp,
                        iconOffsetY = iconYCorrection, // 밀도에 따른 보정값 적용
                        textOffsetX = textXCorrection // 텍스트 X 보정값 적용
                    )
                }
            }
        }
    }
}