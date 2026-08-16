package com.konkuk.moru.core.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun MoruButtonTypeA(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (enabled) colors.charcoalBlack else colors.mediumGray
    val textColor = if (enabled) colors.paleLime else Color.White
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor, shape = RoundedCornerShape(10.dp))
            .clickable(
                indication = null,
                interactionSource = null,
                enabled = enabled
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.body_SB_16,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun SignUpButtonPreview() {
    MoruButtonTypeA(text = "회원가입", enabled = true) {}
}