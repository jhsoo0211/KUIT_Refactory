package com.konkuk.moru.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun IntroductionField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor = if (value.isEmpty()) {
        if (isFocused) colors.mediumGray else colors.lightGray
    } else {
        colors.limeGreen
    }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = typography.desc_M_14,
        cursorBrush = SolidColor(colors.limeGreen),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .border(1.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
            .background(Color.Transparent)
            .focusable(interactionSource = interactionSource),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "자기소개",
                        style = typography.desc_M_14,
                        color = colors.mediumGray
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview
@Composable
private fun IntroductionFieldPreview() {
    IntroductionField(value = "") { }
}