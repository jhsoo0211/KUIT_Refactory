package com.konkuk.moru.presentation.myactivity.component

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import kotlin.math.exp
import kotlin.math.pow

@Composable
fun InsightGraphA(
    averageScore: Float = 50f,
    myScore: Float = 70f,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(14.dp))
        Row() {
            Text(text = "내 루틴 실천률은 ", style = typography.body_SB_16, color = colors.black)
            Text(
                text = myScore.toInt().toString() + "점",
                style = typography.body_SB_16,
                color = colors.limeGreen
            )
            Text(text = "이에요", style = typography.body_SB_16, color = colors.black)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row() {
            Text(text = "사용자 평균 대비 ", style = typography.desc_M_12, color = colors.darkGray)
            if (averageScore > myScore) {
                Text(
                    text = (averageScore - myScore).toInt().toString() + "점 ",
                    style = typography.desc_M_12,
                    color = Color(0xFF0300B2)
                )
                Text(text = "낮아요. 분발해요!", style = typography.desc_M_12, color = colors.darkGray)
            }
            if (averageScore < myScore) {
                Text(
                    text = (myScore - averageScore).toInt().toString() + "점 ",
                    style = typography.desc_M_12,
                    color = colors.red
                )
                Text(
                    text = "높아요. 우수한 상태시네요!",
                    style = typography.desc_M_12,
                    color = colors.darkGray
                )
            }
            if (averageScore == myScore) {
                Text(text = "동일해요", style = typography.desc_M_12, color = colors.darkGray)
            }
        }

        Spacer(modifier = Modifier.height(44.79.dp))

        val limeGreen = colors.limeGreen
        val oliveGreen = colors.oliveGreen
        val mediumGray = colors.mediumGray

        val context = LocalContext.current
        val density = LocalDensity.current

        val customTypeface = remember {
            ResourcesCompat.getFont(context, R.font.pretendard_medium) ?: Typeface.DEFAULT
        }

        val fontSizePx = with(density) { 12.sp.toPx() }

        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(105.dp)
                .padding(horizontal = 27.5.dp)
        ) {
            val width = constraints.maxWidth.toFloat()
            val height = constraints.maxHeight.toFloat()

            val path = Path()
            val steps = 100
            val sigma = 20f
            val mu = 50f

            val xToScreen = { x: Float -> x / 100f * width }
            val yFromNormal = { x: Float ->
                val exponent = -((x - mu).pow(2)) / (2 * sigma.pow(2))
                exp(exponent)
            }

            val yValues = (0..steps).map { x -> yFromNormal(x.toFloat()) }
            val yMax = yValues.maxOrNull() ?: 1f

            val normalizedPoints = (0..steps).map { x ->
                val fx = x.toFloat()
                val normY = yFromNormal(fx) / yMax
                Offset(xToScreen(fx), height * (1f - 1f * normY))
            }

            path.moveTo(normalizedPoints.first().x, normalizedPoints.first().y)
            for (i in 1 until normalizedPoints.size) {
                val prev = normalizedPoints[i - 1]
                val curr = normalizedPoints[i]
                val mid = Offset((prev.x + curr.x) / 2, (prev.y + curr.y) / 2)
                path.quadraticBezierTo(prev.x, prev.y, mid.x, mid.y)
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        listOf(Color(0xCCB8EE44), Color(0x99FFFFFF))
                    ),
                    style = Fill
                )

                drawPath(
                    path = path,
                    color = limeGreen,
                    style = Stroke(width = 2.dp.toPx())
                )

                val avgX = xToScreen(averageScore)
                val avgY = getYFromX(normalizedPoints, avgX)
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = Offset(avgX, avgY)
                )
                drawCircle(
                    color = limeGreen,
                    radius = 6.dp.toPx(),
                    center = Offset(avgX, avgY),
                    style = Stroke(width = 2.dp.toPx())
                )

                val myX = xToScreen(myScore)
                val myY = getYFromX(normalizedPoints, myX)
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = Offset(myX, myY),
                )
                drawCircle(
                    color = oliveGreen,
                    radius = 6.dp.toPx(),
                    center = Offset(myX, myY),
                    style = Stroke(width = 2.dp.toPx())
                )

                drawContext.canvas.nativeCanvas.apply {
                    val labelPaint = Paint().apply {
                        color = mediumGray.toArgb()
                        textSize = fontSizePx
                        typeface = customTypeface
                        isAntiAlias = true
                        textAlign = Paint.Align.CENTER
                    }

                    drawText("평균", avgX, avgY - 15.dp.toPx(), labelPaint)
                    drawText("내 위치", myX, myY - 15.dp.toPx(), labelPaint)
                }
            }
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
        ) {
            val width = constraints.maxWidth.toFloat()
            val steps = 5

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)
            ) {
                for (i in 0..steps) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${i * 20}",
                            color = colors.mediumGray,
                            style = typography.body_SB_14
                        )
                    }
                }
            }
        }
    }
}

fun getYFromX(points: List<Offset>, x: Float): Float {
    for (i in 1 until points.size) {
        val prev = points[i - 1]
        val next = points[i]
        if (x in prev.x..next.x) {
            val t = (x - prev.x) / (next.x - prev.x)
            return prev.y * (1 - t) + next.y * t
        }
    }
    return points.last().y
}
