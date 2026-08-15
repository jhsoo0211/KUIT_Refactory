package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun TutorialHintWithLineByPx(
    text: String,
    textOffset: Offset,
    lineOffset: Offset,
    lineLengthPx: Float,
    isUpward: Boolean
) {
    DotWithLine(
        startX = with(LocalDensity.current) { lineOffset.x.toDp() },
        startY = with(LocalDensity.current) { lineOffset.y.toDp() },
        length = with(LocalDensity.current) { lineLengthPx.toDp() },
        isUpward = isUpward
    )
    Text(
        text = text,
        style = typography.time_R_14.copy(fontWeight = FontWeight.Bold),
        color = Color.White,
        modifier = Modifier.offset(
            x = with(LocalDensity.current) { textOffset.x.toDp() },
            y = with(LocalDensity.current) { textOffset.y.toDp() }
        )
    )
}
