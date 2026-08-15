package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun MyActGenderInputField(
    selected: String,                 // "남자" 또는 "여자" 또는 ""
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text("성별", color = colors.black, style = typography.body_SB_16, modifier = Modifier.padding(bottom = 6.dp))
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(colors.veryLightGray)
                .height(45.dp)
                .padding(4.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                listOf("남자", "여자").forEachIndexed { idx, gender ->
                    val isSelected = selected.startsWith(gender.take(1)) // "남", "여" 대응
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) colors.paleLime else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) colors.limeGreen else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { onSelect(gender) }
                    ) {
                        Text(
                            text = gender,
                            color = if (isSelected) colors.oliveGreen else colors.mediumGray,
                            style = typography.desc_M_14
                        )
                    }
                    if (idx == 0) Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}
