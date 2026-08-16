package com.konkuk.moru.core.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme


/**
 * Moru 디자인 시스템의 주요 액션 버튼
 * @param fontsize 폰트 크기
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트
 * @param backgroundColor 버튼 배경색
 * @param contentColor 버튼 내용(텍스트, 아이콘) 색상
 * @param modifier Modifier
 * @param enabled 활성화 여부
 * @param shape 버튼 모양 (기본값: 12dp 둥근 사각형)
 * @param disabledBackgroundColor 비활성화 시 배경색
 * @param disabledContentColor 비활성화 시 내용 색상
 * @param iconContent 아이콘을 위한 Composable 슬롯
 */

@Composable
fun MoruButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    textStyle: TextStyle? = null,
    contentColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    disabledBackgroundColor: Color = Color.LightGray,
    disabledContentColor: Color = Color.Gray,
    iconContent: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledContentColor
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconContent != null) {
                iconContent()
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            }
            Text(
                text = text,
                // 2. Elvis 연산자(?:)를 사용해 textStyle이 null이면 MaterialTheme 값을 사용합니다.
                style = textStyle ?: MORUTheme.typography.body_SB_16
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonShapeExamples() {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. 기본값 (모서리가 12.dp 둥근 사각형)
        MoruButton(
            text = "기본 버튼",
            onClick = { },
            backgroundColor = Color(0xFFB8EE44),
            contentColor = Color.Black,
            iconContent = { // ◀ iconContent 람다에 아이콘을 넣어줍니다.
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "추가 아이콘"
                )
            }

        )


        // 팔로우(활성화) 예시
        MoruButton(
            text = "팔로우",
            onClick = { },
            backgroundColor = Color.Black,
            contentColor = MORUTheme.colors.limeGreen,
            shape = RoundedCornerShape(size = 140.dp), // ◀ 잘린 모양으로 지정
            modifier=Modifier.size(width=137.dp,height =37.dp)
        )


        // 팔로우(활성화) 예시
        MoruButton(
            text = "팔로잉",
            onClick = { },
            backgroundColor = Color(0xFFF1F3F5),
            contentColor = MORUTheme.colors.mediumGray,
            shape = RoundedCornerShape(size = 140.dp), // ◀ 잘린 모양으로 지정
            modifier=Modifier.size(width=137.dp,height =37.dp)
        )

        //interactive mode활용하면 보입니다.
        // 1. '팔로우' 상태를 저장할 변수를 remember로 만듭니다.
        var isFollowing by remember { mutableStateOf(false) }

        // 2. isFollowing 상태에 따라 버튼의 텍스트, 배경색, 글자색을 결정합니다.
        val buttonText = if (isFollowing) "팔로잉" else "팔로우"
        val backgroundColor = if (isFollowing) MORUTheme.colors.veryLightGray else Color.Black
        val contentColor = if (isFollowing) MORUTheme.colors.mediumGray else Color(0xFFB8EE44)

        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            MoruButton(
                text = buttonText, // ◀ 상태에 따라 결정된 텍스트
                backgroundColor = backgroundColor, // ◀ 상태에 따라 결정된 배경색
                contentColor = contentColor,       // ◀ 상태에 따라 결정된 글자색
                shape = RoundedCornerShape(140.dp),
                modifier = Modifier.size(width=137.dp,height =37.dp),
                // 3. 클릭 시 isFollowing 상태를 반전시킵니다.
                onClick = {
                    isFollowing = !isFollowing
                }
            )
        }



// 3. 아이콘이 있는 비활성화 버튼 예시
        MoruButton(
            text = "추가 불가",
            enabled = false, // ◀ 비활성화
            onClick = { /* 호출되지 않음 */ },
            backgroundColor = Color(0xFF00796B),
            contentColor = Color.White,
            iconContent = { // ◀ 아이콘도 함께 비활성 색상으로 표시됩니다.
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "추가 아이콘"
                )
            }
        )
    }
}