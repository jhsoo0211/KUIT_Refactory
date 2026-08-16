package com.konkuk.moru.core.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme // 기존 테마를 그대로 사용

@Composable
fun ConstantSizeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // 스위치 트랙(배경)의 색상을 애니메이션으로 처리
    val trackColor by animateColorAsState(
        targetValue =  if (checked) MORUTheme.colors.darkGray else MORUTheme.colors.lightGray,
        animationSpec = tween(durationMillis = 300),
        label = "trackColor"
    )

    // 스위치 동그란 원(Thumb)의 위치를 애니메이션으로 처리
    // -1.0f는 왼쪽 끝, 1.0f는 오른쪽 끝을 의미
    val alignment by animateAlignmentAsState(
        targetAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
        animationSpec = tween(durationMillis = 300),
        label = "alignment"
    )

    Box(
        modifier = modifier
            .width(52.dp)
            .height(28.dp)
            .clip(CircleShape) // 전체 모양을 둥글게
            .background(trackColor) // 애니메이션 색상 적용
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 클릭 시 물결 효과 제거
            ) {
                onCheckedChange(!checked)
            },
        contentAlignment = alignment // 정렬 위치를 애니메이션 값으로 설정
    ) {
        // 동그란 원 (Thumb)
        Box(
            modifier = Modifier
                .size(22.dp) // 원의 크기 고정
                .padding(2.dp)
                .background(
                    color = MORUTheme.colors.mediumGray, // 원의 색상은 항상 동일
                    shape = CircleShape
                )
        )
    }
}

// animateAlignmentAsState 헬퍼 함수
@Composable
private fun animateAlignmentAsState(
    targetAlignment: Alignment,
    animationSpec: androidx.compose.animation.core.AnimationSpec<Float>,
    label: String
): State<Alignment> {
    val bias by animateFloatAsState(
        targetValue = if (targetAlignment == Alignment.CenterEnd) 1f else -1f,
        animationSpec = animationSpec,
        label = label
    )
    return remember(bias) {
        derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
    }
}


@Composable
@Preview(showBackground = true)
fun CustomSwitchScreen() {
    var checked by remember { mutableStateOf(true) }
    MORUTheme { // 미리보기에서도 테마를 적용해야 색상을 올바르게 참조할 수 있습니다.
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ConstantSizeSwitch(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
    }
}