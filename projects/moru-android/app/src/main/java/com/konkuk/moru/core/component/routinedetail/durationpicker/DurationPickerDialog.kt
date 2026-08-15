package com.konkuk.moru.core.component.routinedetail.durationpicker

import android.view.HapticFeedbackConstants
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun DurationPickerDialog(
    onConfirm: (Int, Int, Int) -> Unit,
    onDismiss: () -> Unit,
    initialTime: String? = null // "HH:mm:ss" 또는 null
) {
    val (initH, initM, initS) = parseHms(initialTime)

    var hour by remember { mutableIntStateOf(initH) }
    var minute by remember { mutableIntStateOf(initM) }
    var second by remember { mutableIntStateOf(initS) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.80f)
                .height(280.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(top = 22.dp, bottom = 10.dp, start = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = "소요 시간 선택",
                style = typography.title_B_20,
                color = colors.black
            )

            // Wheels
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                // 중앙 가이드 라인
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(Color(0x0F000000))
                        .align(Alignment.Center)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AndroidNumberPicker(
                        value = hour,
                        range = 0..23,
                        label = "시",
                        onChanged = { hour = it }
                    )
                    AndroidNumberPicker(
                        value = minute,
                        range = 0..59,
                        label = "분",
                        onChanged = { minute = it }
                    )
                    AndroidNumberPicker(
                        value = second,
                        range = 0..59,
                        label = "초",
                        onChanged = { second = it }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(colors.lightGray, RoundedCornerShape(10.dp))
                        .noIndicationClickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "닫기", style = typography.desc_M_16, color = colors.mediumGray)
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(colors.limeGreen, RoundedCornerShape(10.dp))
                        .noIndicationClickable { onConfirm(hour, minute, second) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "확인", style = typography.desc_M_16, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun AndroidNumberPicker(
    value: Int,
    range: IntRange,
    label: String,
    onChanged: (Int) -> Unit
) {
    AndroidView(
        modifier = Modifier.height(160.dp),
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                wrapSelectorWheel = true
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                // 두 자리 포맷
                setFormatter { v -> String.format("%02d", v) }
                // 텍스트 크기/색은 테마에 맞춰 시스템이 칠하지만, 제조사별 차이가 있음
                setOnValueChangedListener { view, _, newVal ->
                    // 스크롤 중 값 변경 시 가벼운 햅틱
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                    onChanged(newVal)
                }
            }
        },
        update = { picker ->
            if (picker.value != value) picker.value = value
        }
    )
}

private fun parseHms(hms: String?): Triple<Int, Int, Int> {
    return if (hms != null && Regex("""\d{2}:\d{2}:\d{2}""").matches(hms)) {
        val parts = hms.split(":").map { it.toIntOrNull() ?: 0 }
        Triple(parts[0], parts[1], parts[2])
    } else Triple(0, 0, 0)
}

private fun Modifier.noIndicationClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) { onClick() }
}