package com.konkuk.moru.presentation.routinefeed.component.follow

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.konkuk.moru.R
import com.konkuk.moru.core.component.button.MoruButton
import com.konkuk.moru.presentation.routinefeed.data.FollowUser
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.moruFontMedium

@Composable
fun UserItem(
    user: FollowUser,
    showFollowButton: Boolean,
    onFollowClick: (FollowUser) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    followEnabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick(user.id) }
            .padding(horizontal = 16.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필 이미지 (임시 플레이스홀더)
        AsyncImage(
            model = user.profileImageUrl, // URL을 모델로 전달
            contentDescription = "Profile Picture",
            // 로딩 중에 보여줄 이미지
            placeholder = painterResource(id = R.drawable.ic_profile_with_background),
            // URL이 null이거나 에러 발생 시 보여줄 이미지
            error = painterResource(id = R.drawable.ic_profile_with_background),
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 사용자 정보 (이름, 자기소개)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.username,
                fontFamily = moruFontMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = Color.Black
            )
            Text(
                text = user.bio,
                fontSize = 10.sp,
                fontWeight = FontWeight(400),
                lineHeight = 16.sp,
                color = Color.DarkGray,
                maxLines = 1, // 텍스트를 최대 1줄로 제한
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(36.dp))

        // 팔로우/팔로잉 버튼(하드 코딩)
        val buttonText = if (user.isFollowing) "팔로잉" else "팔로우"
        val backgroundColor = if (user.isFollowing) Color(0xFFF1F3F5) else Color.Black
        val contentColor =
            if (user.isFollowing) MORUTheme.colors.mediumGray else MORUTheme.colors.limeGreen

        if (showFollowButton) {
            MoruButton(
                text = buttonText,
                onClick = { if (followEnabled) onFollowClick(user) },
                enabled = followEnabled, // ✅ 하드 가드(시각 + 클릭 차단)
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                shape = RoundedCornerShape(140.dp),
                textStyle = MORUTheme.typography.title_B_14,
                modifier = Modifier
                    .wrapContentWidth() // 텍스트 크기에 따라 폭 조정
                    .defaultMinSize(minWidth = 64.dp, minHeight = 32.dp)


            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun UserItemPreview() {
    MORUTheme {
        Column {
            UserItem(
                user = FollowUser("", "사용자명1", "사용자명 1", "자기소개입니다. 잘 부탁드립니다.", false),
                onFollowClick = {},
                onUserClick = {},
                showFollowButton = true,
                followEnabled = true
            )
            UserItem(
                user = FollowUser("", "", "사용자명2", "안녕하세요!", true),
                onFollowClick = {},
                onUserClick = {},
                showFollowButton = true,
                followEnabled = false
            )
        }
    }
}