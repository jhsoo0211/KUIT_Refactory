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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun MoruButtonStart(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = colors.limeGreen, shape = RoundedCornerShape(10.dp))
            .clickable(
                indication = null,
                interactionSource = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "시작하기",
            style = typography.body_SB_16
        )
    }
}

@Preview
@Composable
private fun MoruButtonStartPreview() {
    MoruButtonStart(onClick = {})
}