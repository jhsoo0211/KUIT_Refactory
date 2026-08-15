package com.konkuk.moru.presentation.myactivity.component

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.core.util.modifier.noRippleClickable
import com.konkuk.moru.presentation.myactivity.screen.RoutineDetail
import com.konkuk.moru.presentation.myactivity.screen.RoutineStatus
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import java.time.Duration
import java.time.format.DateTimeFormatter

/* ---------- formatters & utils ---------- */

private val RANGE_START_FMT: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy. MM. dd a hh:mm")
private val TIME_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private fun Duration.toKoreanMinSec(): String {
    val total = seconds.coerceAtLeast(0)
    val m = (total / 60)
    val s = (total % 60)
    return "%d분 %02d초".format(m, s)
}

/* ---------- public composable ---------- */

@Composable
fun RoutineDetailContent(
    detail: RoutineDetail,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HeaderSection(detail = detail, onBack = { navController.popBackStack() })

        Spacer(Modifier.height(26.dp))

        StepsSection(
            steps = detail.steps,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}

/* ---------- header ---------- */

@Composable
private fun HeaderSection(
    detail: RoutineDetail,
    onBack: () -> Unit,
) {
    val isDone = detail.status == RoutineStatus.DONE

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(455.dp)
            .background(colors.veryLightGray)
    ) {
        // background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.55f to Color(0xFFF5F5F5),
                        0.75f to Color(0xFFD9D9D9),
                        0.90f to Color(0xFFBBBBBB),
                        1.0f to Color(0xFF999999)
                    )
                )
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_d),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp)
        ) {
            // 제목 + 상태칩
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = detail.title,
                    style = typography.title_B_24,
                    color = colors.darkGray
                )
                Spacer(Modifier.width(17.dp))
                StatusChip(isDone = isDone)
            }

            Spacer(Modifier.height(14.dp))
            // 태그
            Row {
                detail.tags.forEach {
                    TagPill(text = it)
                    Spacer(Modifier.width(8.dp))
                }
            }

            Spacer(Modifier.height(11.dp))
            LabeledRow(
                label = "TOTAL",
                value = detail.totalDuration.toKoreanMinSec()
            )

            Spacer(Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(109.dp)
            ) {
                Text("RESULT", style = typography.time_R_14, color = Color.White)
                CompleteCheck(isDone) // ✅ 완료/미완료
            }

            Spacer(Modifier.height(6.dp))
            val startTxt = detail.dateRange.start.format(RANGE_START_FMT)
            val endTxt = detail.dateRange.end.format(DateTimeFormatter.ofPattern("hh:mm"))
            LabeledRow(
                label = "DATE",
                // ex) 2025. 05. 09 오전 08:00 ~ 09:10
                value = "$startTxt ~ $endTxt",
                width = 266.dp
            )

            Spacer(Modifier.height(11.dp))
        }
    }
}

@Composable
private fun StatusChip(isDone: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(47.dp)
            .height(28.dp)
            .background(
                color = colors.paleLime,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            text = if (isDone) "완료" else "집중",
            color = colors.oliveGreen,
            style = typography.body_SB_16
        )
    }
}

@Composable
private fun TagPill(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(18.dp)
            .background(color = colors.darkGray, shape = RoundedCornerShape(140.dp))
    ) {
        Text(
            text = text,
            color = colors.paleLime,
            style = typography.time_R_14,
            modifier = Modifier.padding(horizontal = 7.dp)
        )
    }
}

@Composable
private fun LabeledRow(label: String, value: String, width: Dp = 130.dp) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.width(width)
    ) {
        Text(label, style = typography.time_R_14, color = Color.White)
        Text(value, style = typography.title_B_14, color = Color.White)
    }
}

/* ---------- steps ---------- */

@Composable
private fun StepsSection(
    steps: List<com.konkuk.moru.presentation.myactivity.screen.RoutineStep>,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateMapOf<Int, Boolean>() }

    Column(modifier = modifier) {
        Text(text = "STEP", style = typography.title_B_20, color = colors.black)
        Spacer(Modifier.height(8.dp))

        steps.forEach { step ->
            val isExpanded = expanded[step.order] == true

            Column(Modifier.fillMaxWidth()) {
                DividerGray()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.noRippleClickable {
                            expanded[step.order] = !isExpanded
                        }
                    ) {
                        Spacer(Modifier.width(16.dp))
                        Text("${step.order}", style = typography.body_SB_14, color = colors.black)
                        Spacer(Modifier.width(41.dp))
                        Text(step.name, style = typography.body_SB_14, color = colors.black)
                        Spacer(Modifier.width(12.5.dp))
                        Icon(
                            imageVector = if (isExpanded)
                                Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(end = 13.dp)
                    ) {
                        Text(step.startTime.format(TIME_FMT), style = typography.body_SB_14, color = colors.black)
                        step.endTime?.let {
                            Text(
                                it.format(TIME_FMT),
                                style = typography.caption_L_12.copy(fontSize = 10.sp),
                                color = colors.mediumGray
                            )
                        }
                    }
                }

                DividerGray()

                AnimatedVisibility(visible = isExpanded) {
                    StepMemoRow(memo = step.memo)
                }

                if (!isExpanded) Spacer(Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun StepMemoRow(memo: String?) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val memoText = memo ?: "메모가 없습니다."

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(69.dp)
            .background(colors.veryLightGray)
            .padding(top = 12.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = memoText,
            style = typography.desc_M_12,
            color = colors.darkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.width(4.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_copy_button),
            contentDescription = "Copy memo",
            modifier = Modifier
                .size(16.dp)
                .clickable {
                    clipboardManager.setText(AnnotatedString(memoText))
                    Toast.makeText(context, "복사되었습니다", Toast.LENGTH_SHORT).show()
                }
        )
    }
}

@Composable
private fun DividerGray() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colors.mediumGray)
    )
}
