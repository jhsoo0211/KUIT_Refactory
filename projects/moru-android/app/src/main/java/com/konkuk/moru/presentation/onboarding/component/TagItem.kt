package com.konkuk.moru.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun TagItem(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) colors.paleLime else colors.veryLightGray
    val textColor = if (isSelected) colors.oliveGreen else colors.mediumGray
    val borderModifier = if (isSelected) {
        Modifier.border(
            width = 1.dp,
            color = colors.oliveGreen,
            shape = RoundedCornerShape(20.dp)
        )
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(backgroundColor, shape = RoundedCornerShape(20.dp))
            .then(borderModifier)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            style = typography.time_R_14,
            color = textColor
        )
    }
}

@Preview(name = "선택되지 않은 태그")
@Composable
private fun TagItemUnselectedPreview() {
    TagItem(
        tag = "#운동",
        isSelected = false,
        onClick = {}
    )
}

@Preview(name = "선택된 태그")
@Composable
private fun TagItemSelectedPreview() {
    TagItem(
        tag = "#공부",
        isSelected = true,
        onClick = {}
    )
}