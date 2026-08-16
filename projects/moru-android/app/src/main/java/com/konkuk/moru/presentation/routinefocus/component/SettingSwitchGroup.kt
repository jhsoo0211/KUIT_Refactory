package com.konkuk.moru.presentation.routinefocus.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.ConstantSizeSwitch
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun SettingSwitchGroup(
    settings: List<Triple<String, () -> Boolean, (Boolean) -> Unit>>
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFFFFF).copy(alpha = 0.75f))
    ) {
        settings.forEachIndexed { index, (title, checked, onCheckedChange) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = typography.desc_M_14,
                    color = colors.black
                )
                ConstantSizeSwitch(
                    checked = checked(),
                    onCheckedChange = onCheckedChange
                )
            }

            if (index < settings.lastIndex) {
                Divider(color = colors.mediumGray.copy(alpha = 0.3f), thickness = 3.dp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun SettingSwitchGroupPreview() {
    val isDarkMode = remember { mutableStateOf(false) }
    val isDoNotDisturb = remember { mutableStateOf(true) }
    val isStepVibration = remember { mutableStateOf(false) }
    val isLandscapeMode = remember { mutableStateOf(false) }

    MORUTheme { // 꼭 MORUTheme 안에서 preview해야 색상 정상 적용
        SettingSwitchGroup(
            settings = listOf(
                Triple("다크 모드", { isDarkMode.value }) { isDarkMode.value = it },
                Triple("방해 금지 모드", { isDoNotDisturb.value }) { isDoNotDisturb.value = it },
                Triple("스텝 완료 진동", { isStepVibration.value }) { isStepVibration.value = it },
                Triple("가로 모드", { isLandscapeMode.value }) { isLandscapeMode.value = it }
            )
        )
    }
}

