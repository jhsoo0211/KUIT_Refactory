package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import kotlin.math.ceil
import kotlin.math.max

@Composable
fun InsightGraphC(
    morning: Float,
    afternoon: Float,
    night: Float,
    lateNight: Float,
    modifier: Modifier = Modifier
) {
    val labels = listOf("오전", "오후", "밤", "심야")
    val values = listOf(morning, afternoon, night, lateNight)

    val axisMax = max(1, ceil(values.maxOrNull() ?: 0f).toInt())

    val barMaxHeight = 149.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "시간대별 루틴 실천개수",
            style = typography.body_SB_16,
            color = colors.black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(17.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(167.dp)
                .padding(end = 28.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(30.dp)
                    .padding(bottom = 17.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                for (i in axisMax downTo 0) {
                    Text(
                        text = "$i",
                        color = colors.darkGray,
                        style = typography.body_SB_16.copy(fontSize = 9.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                values.forEachIndexed { index, value ->
                    val ratio = (value / axisMax.toFloat()).coerceIn(0f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(35.dp)
                                .height(barMaxHeight * ratio)
                                .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(colors.limeGreen, Color.Transparent)
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = labels[index],
                            style = typography.body_SB_16.copy(fontSize = 9.sp),
                            color = colors.darkGray
                        )
                    }
                }
            }
        }
    }
}
