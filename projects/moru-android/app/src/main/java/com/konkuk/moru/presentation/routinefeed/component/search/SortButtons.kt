package com.konkuk.moru.presentation.routinefeed.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme


@Composable
internal fun SortButtons(selectedOption: String, onOptionSelected: (String) -> Unit) {


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SortButton("최신순", selectedOption == "최신순") { onOptionSelected("최신순") }
        Spacer(modifier = Modifier.size(8.dp))
        SortButton("인기순", selectedOption == "인기순") { onOptionSelected("인기순") }
    }
}

@Composable
private fun SortButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color.Black else MORUTheme.colors.veryLightGray
    val textColor = if (isSelected) Color.White else MORUTheme.colors.mediumGray
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = textColor, style = MORUTheme.typography.body_SB_14)
    }
}
