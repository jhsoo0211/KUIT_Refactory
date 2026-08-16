package com.konkuk.moru.presentation.routinefeed.component.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.ui.theme.moruFontSemiBold


@Composable
fun TooltipBubble(
    modifier: Modifier = Modifier,
    shape: Shape,
    backgroundColor: Color = Color.Black,
    tailHeight: Dp = 0.dp, // 꼬리 높이를 인자로 받도록 추가
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = backgroundColor, shape = shape),
        horizontalAlignment = Alignment.CenterHorizontally // 내부 콘텐츠를 수평 중앙 정렬합니다.
    ) {
        // [수정] 꼬리 높이만큼의 공간을 Spacer로 명시적으로 확보합니다.
        Spacer(modifier = Modifier.height(tailHeight))

        content()
    }
}


@Preview(showBackground = true)
@Composable
fun SpecificTooltipPreview() {
    // 1. 요구사항에 맞는 커스텀 Shape 생성
    val customBubbleShape = TooltipShape(
        cornerRadius = 8.dp,
        tailHorizontalOffsetFromEnd = 38.dp
    )

    // 2. TooltipBubble 호출 시 크기, 모양, 색상, 그리고 꼬리 높이를 전달
    Box(modifier = Modifier.padding(20.dp)) {
        TooltipBubble(
            modifier = Modifier,
                //.width(328.dp),
            shape = customBubbleShape,
            backgroundColor = Color.Black,
            tailHeight = 11.56811.dp
        ) {
            // 3. 내부 글씨는 수정 없이 그대로 사용
            Text(
                text = "시간대가 설정되지 않은 루틴은 인사이트 분석 시에 제외됩니다.\n정확한 인사이트를 원한다면 시간대를 설정해보세요!",
                color = Color.White,
                fontFamily = moruFontSemiBold,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
            )
        }
    }
}