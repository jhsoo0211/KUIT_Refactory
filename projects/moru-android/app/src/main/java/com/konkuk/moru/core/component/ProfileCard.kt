package com.konkuk.moru.core.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R


@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    username: String,
    tag: String,
    backgroundColor: Color? = null // 변경점: 배경색 파라미터 추가 (기본값 null)
) {
    Column(
        modifier = modifier
            //.fillMaxHeight()
            // 변경점: backgroundColor가 null이 아닐 때만 배경색을 적용
            .then(
                if (backgroundColor != null) Modifier.background(backgroundColor)
                else Modifier
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painter,
            contentDescription = "User Profile",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier=Modifier.padding(2.dp))
        Text(text = username, fontSize = 12.sp, lineHeight = 16.sp)
        Text(text = tag, color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
    }
}

// 변경점: 두 가지 경우를 모두 테스트하는 Preview 추가
@Preview(name = "With Background", showBackground = true)
@Composable
fun ProfileCardWithBgPreview() {
    MaterialTheme {
        ProfileCard(
            painter = painterResource(id = R.drawable.ic_avatar),
            username = "사용자명",
            tag = "#운동하자",
            backgroundColor = Color(0xFFF5F5F5) // 배경색 지정
        )
    }
}

@Preview(name = "Without Background", showBackground = true)
@Composable
fun ProfileCardWithoutBgPreview() {
    MaterialTheme {
        ProfileCard(
            painter = painterResource(id = R.drawable.ic_avatar),
            username = "사용자명",
            tag = "#운동하자"
            // backgroundColor를 생략하면 기본값인 null이 사용됨
        )
    }
}