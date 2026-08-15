package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun RoutineStepItem(
    modifier: Modifier = Modifier,
    index: Int,
    title: String,
    duration: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showDuration: Boolean = true,
    showSwitch: Boolean = true
) {
    Box(
        modifier = modifier
            .height(36.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 18.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${index + 1}",
                style = typography.desc_M_14,
                color = colors.black
            )
            Spacer(modifier = modifier.width(29.dp))
            Text(
                text = title,
                style = typography.desc_M_16,
                color = colors.black,
                modifier = Modifier.weight(1f)
            )
            if (showDuration) {
                Text(
                    text = "${duration}m",
                    style = typography.desc_M_14,
                    color = colors.black
                )
                Spacer(modifier = modifier.width(20.dp))
            }
            if (showSwitch) {
                Switch(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier
                        .scale(scaleX = 0.7f, scaleY = 0.7f), // 가로 70%, 세로 50%로 슬림하게
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = colors.mediumGray,
                        uncheckedTrackColor = colors.lightGray,
                        checkedThumbColor = colors.mediumGray,
                        checkedTrackColor = Color(0xFF1A1A1A),
                        uncheckedBorderColor = Color.Transparent,
                        checkedBorderColor = Color.Transparent
                    ),
                    thumbContent = {
                        val thumbSize = if (isChecked) 8.dp else 10.dp  // 전체적으로 더 작게 조정
                        Box(
                            modifier = Modifier.size(thumbSize)
                        )
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun RoutineStepItemPreview() {

    RoutineStepItem(
        index = 0,
        title = "샤워하기",
        duration = 15,
        isChecked = false,
        onCheckedChange = {},
        showDuration = true,
        showSwitch = true,
    )
}