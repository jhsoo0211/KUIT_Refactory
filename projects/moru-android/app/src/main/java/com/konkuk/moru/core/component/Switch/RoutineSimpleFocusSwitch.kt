package com.konkuk.moru.core.component.Switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun RoutineSimpleFocusSwitch(
    checked: Boolean,
    onClick: (Boolean) -> Unit
) {
    // 애니메이션: thumb 이동 (왼쪽 -1f ~ 오른쪽 1f)
    val targetBias = if (checked) 1f else -1f
    val bias by animateFloatAsState(targetValue = targetBias, animationSpec = tween(250))

    // 텍스트 색상 애니메이션
    val leftTextColor by animateColorAsState(
        targetValue = if (!checked) colors.textLime else colors.mediumGray,
        animationSpec = tween(250)
    )
    val rightTextColor by animateColorAsState(
        targetValue = if (checked) colors.textLime else colors.mediumGray,
        animationSpec = tween(250)
    )

    Box(
        modifier = Modifier
            .width(89.dp)
            .height(26.dp)
            .clip(CircleShape)
            .background(color = colors.lightGray)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(!checked)
            },
        contentAlignment = Alignment.Center
    ) {
        // Thumb
        Box(
            modifier = Modifier
                .fillMaxWidth(0.53f)
                .fillMaxHeight()
                .align(BiasAlignment(horizontalBias = bias, verticalBias = 0f))
                .background(Color(0xFFEBFFC0), CircleShape)
        )

        // 텍스트 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = "간편",
                modifier = Modifier.weight(1f),
                style = typography.body_SB_16,
                color = leftTextColor,
                textAlign = TextAlign.Center
            )
            Text(
                text = "집중",
                modifier = Modifier.weight(1f),
                style = typography.body_SB_16,
                color = rightTextColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleModeSwitchPreview() {
    var isChecked by remember { mutableStateOf(false) }

    RoutineSimpleFocusSwitch(
        checked = isChecked,
        onClick = { isChecked = it }
    )
}