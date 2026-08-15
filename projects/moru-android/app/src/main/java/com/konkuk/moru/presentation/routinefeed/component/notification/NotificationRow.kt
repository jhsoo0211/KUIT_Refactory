package com.konkuk.moru.presentation.routinefeed.component.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
fun NotificationRow(
    senderId: String,
    nickname: String,
    profileUrl: String?,
    message: String,
    relativeTime: String,
    onProfileClick: (userId: String) -> Unit
) {
    // 1. 서버에서 받은 전체 메시지(message)에서 닉네임(nickname) 부분만 강조 처리
    val annotatedMessage = buildAnnotatedString {
        append(message)
        val startIndex = message.indexOf(nickname)
        if (startIndex != -1) {
            addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold),
                start = startIndex,
                end = startIndex + nickname.length
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Row 전체를 클릭 가능하게 만들고 프로필 화면으로 이동시킵니다.
            .clickable { onProfileClick(senderId) }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 2. Coil의 AsyncImage를 사용하여 프로필 URL 로드
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profileUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_profile_with_background), // 로딩 중 이미지
            error = painterResource(R.drawable.ic_profile_with_background),       // 에러 시 이미지
            contentDescription = "프로필 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 3. 위에서 만든 강조된 메시지를 Text에 적용
        Text(
            text = annotatedMessage,
            style = MORUTheme.typography.desc_M_16,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 4. 서버에서 받은 상대 시간(relativeTime)을 그대로 사용
        Text(
            text = relativeTime,
            style = MORUTheme.typography.desc_M_12,
            color = Color.Gray
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun NotificationRowCombinedPreview() {
    MORUTheme {
        Surface {
            NotificationRow(
                senderId = "user-123",
                nickname = "MORU",
                profileUrl = null, // 프리뷰에서는 이미지 URL이 없다고 가정
                message = "MORU님이 회원님의 '아침 조깅' 루틴을 좋아합니다.",
                relativeTime = "2시간 전",
                onProfileClick = { _ -> }
            )
        }
    }
}
