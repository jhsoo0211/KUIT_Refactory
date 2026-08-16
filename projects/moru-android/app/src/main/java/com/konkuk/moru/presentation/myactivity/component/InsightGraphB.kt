package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun InsightGraphB(
    userName: String,
    weekdayUser: Float,
    weekdayAll: Float,
    weekendUser: Float,
    weekendAll: Float,
    modifier: Modifier = Modifier
) {
    fun scaledFraction(pct: Float, scaleThresholdPct: Float = 10f): Float {
        val p = pct.coerceIn(0f, 100f)
        return if (p <= scaleThresholdPct) (p / scaleThresholdPct) else (p / 100f)
    }

    fun pctLabel(pct: Float): String = "${pct.coerceIn(0f, 100f).toInt()}%"

    data class RowItem(val label: String, val pct: Float)

    val items = listOf(
        RowItem("평일 : $userName"+"님"+"(${pctLabel(weekdayUser)})", weekdayUser),
        RowItem("평일 : 전체 (${pctLabel(weekdayAll)})", weekdayAll),
        RowItem("주말 : $userName"+"님"+"(${pctLabel(weekendUser)})", weekendUser),
        RowItem("주말 : 전체 (${pctLabel(weekendAll)})", weekendAll)
    )

    Column(
        modifier = modifier
            .padding(horizontal = 29.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(12.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Text(text = "평균 루틴 실천률", style = typography.body_SB_16, color = colors.darkGray)
        }
        Spacer(Modifier.height(18.dp))

        items.forEach { item ->
            val fraction = scaledFraction(item.pct).coerceIn(0f, 1f)

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.label,
                    style = typography.desc_M_12.copy(fontSize = 10.sp),
                    color = colors.darkGray,
                    modifier = Modifier.width(110.dp) // 안내 문구 때문에 약간 늘림
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(50))
                        .background(colors.veryLightGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction)
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(colors.paleLime, colors.limeGreen)
                                )
                            )
                    )
                }
            }
            Spacer(Modifier.height(21.dp))
        }
    }
}
