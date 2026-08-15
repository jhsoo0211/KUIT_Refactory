package com.konkuk.moru.presentation.myactivity.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.core.util.modifier.noRippleClickable
import com.konkuk.moru.presentation.myactivity.viewmodel.InsightViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import kotlin.math.roundToInt

@Composable
fun ActInsightInfoClickScreen(
    vm: InsightViewModel = hiltViewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val ui by vm.ui.collectAsState()
    val progress = ui.routineCompletionRate.toFloat().coerceIn(0f, 1f)

    val badgeRes = when (progress) {
        in 0f..0.3f -> R.drawable.ic_third_badge
        in 0.3f..0.7f -> R.drawable.ic_second_badge
        else -> R.drawable.ic_first_badge
    }

    val highlightColor = colors.paleLime
    val mainText = if (progress >= 0.7f) {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = highlightColor)) {
                append("루틴 페이스 메이커 ")
            }
            withStyle(style = SpanStyle(color = Color.White)) {
                append("달성 ")
            }
            withStyle(style = SpanStyle(color = highlightColor)) {
                append("완료")
            }
        }
    } else {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = highlightColor)) {
                append("루틴 페이스 메이커 ")
            }
            withStyle(style = SpanStyle(color = Color.White)) {
                append("달성까지 ")
            }
            withStyle(style = SpanStyle(color = highlightColor)){
                append("${(100 * (1 - progress)).toInt()}%")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.black)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_e),
                contentDescription = "Back Icon",
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "루틴 페이스", style = typography.body_SB_16, color = Color.White)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Image(
            painter = painterResource(badgeRes),
            contentDescription = "Badge",
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color = colors.veryLightGray)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(50.dp))
                    .background(color = colors.limeGreen)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = mainText,
            style = typography.body_SB_16,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(42.dp))

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(colors.mediumGray))

        Spacer(modifier = Modifier.height(32.dp))

        listOf(
            Triple(R.drawable.ic_third_badge, "잠시 걷는 중", "실천율 0~30%"),
            Triple(R.drawable.ic_second_badge, "간헐적 루틴러", "실천율 30~70%"),
            Triple(R.drawable.ic_first_badge, "페이스 메이커", "실천율 70~100%")
        ).forEach { (resId, title, range) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(resId),
                    contentDescription = title,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = title, style = typography.body_SB_16, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = range, style = typography.desc_M_14, color = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.padding(24.dp))
        }
    }
}