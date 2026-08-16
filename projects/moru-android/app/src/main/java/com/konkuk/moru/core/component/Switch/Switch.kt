package com.konkuk.moru.core.component.Switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 커스텀 토글 스위치 컴포넌트
 *
 * @param checked 현재 스위치 상태 (false: 왼쪽, true: 오른쪽)
 * @param onCheckedChange 스위치 상태 변경 시 호출될 콜백
 * @param leftText 왼쪽 옵션 텍스트
 * @param rightText 오른쪽 옵션 텍스트
 * @param containerColor 스위치 전체 배경 색상
 * @param thumbColor 움직이는 선택 표시(Thumb) 색상
 * @param checkedTextColor 선택된 옵션의 텍스트 색상
 * @param uncheckedTextColor 선택되지 않은 옵션의 텍스트 색상
 * @param fontSize 텍스트의 폰트 크기
 */
@Composable
fun CustomToggleSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    leftText: String,
    rightText: String,
    containerColor: Color,
    thumbColor: Color,
    checkedTextColor: Color,
    uncheckedTextColor: Color,
    fontSize: TextUnit
) {
    // 애니메이션 설정
    val alignment by animateAlignmentAsState(if (checked) 1f else -1f)

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 클릭 시 물결 효과 제거
            ) { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        // 움직이는 선택 표시 (Thumb)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f) // 전체 너비의 절반
                .fillMaxHeight()
                .align(alignment) // 애니메이션 적용된 정렬 사용
                .background(thumbColor, CircleShape)
        )

        // 텍스트
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            // 왼쪽 텍스트
            Text(
                text = leftText,
                color = animateColorAsState(if (!checked) checkedTextColor else uncheckedTextColor).value,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            // 오른쪽 텍스트
            Text(
                text = rightText,
                color = animateColorAsState(if (checked) checkedTextColor else uncheckedTextColor).value,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// 스위치 정렬 애니메이션을 위한 헬퍼 함수
@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue, animationSpec = tween(300))
    return remember { derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) } }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun CustomToggleSwitchPreview() {
    var checked1 by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 예제 1: 간편/집중
        CustomToggleSwitch(
            checked = checked1,
            onCheckedChange = { checked1 = it },
            leftText = "간편",
            rightText = "집중",
            containerColor = Color(0xFFE8E8E8),
            thumbColor = Color(0xFFEBFFC0),
            checkedTextColor = Color(0xFF8CCD00),
            uncheckedTextColor = Color.Gray,
            fontSize = 14.sp,
            modifier=Modifier.width(95.dp).height(26.dp)
        )

        // 예제 2: 내 정보/인사이트
        CustomToggleSwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it },
            leftText = "내 정보",
            rightText = "인사이트",
            containerColor = Color(0xFFF0F0F0),
            thumbColor = Color.White,
            checkedTextColor = Color.Black,
            uncheckedTextColor = Color.LightGray,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(0.8f) // 너비 조절 예시
        )
    }
}