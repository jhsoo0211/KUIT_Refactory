package com.konkuk.moru.presentation.routinefeed.component.Routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.konkuk.moru.R
import com.konkuk.moru.presentation.routinefeed.data.LiveUserInfo
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
fun MoruLiveSection(
    modifier: Modifier = Modifier,
    liveUsers: List<LiveUserInfo>,
    onUserClick: (String) -> Unit,
    onTitleClick: () -> Unit
) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTitleClick() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "MORU LIVE", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(2.dp))
            Image(
                painter = painterResource(id = if (liveUsers.isNotEmpty()) R.drawable.ic_antenna_color else R.drawable.ic_antenna),
                contentDescription = "Live Status",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (liveUsers.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // [수정] 데이터 모델 변경에 따라 `items`의 key를 `userId`로 변경
                items(liveUsers, key = { it.userId }) { user ->
                    // [수정] 기존 ProfileCard와 동일한 UI를 Column과 AsyncImage로 직접 구현
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onUserClick(user.userId) }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.profileImageUrl) // 여기에 이미지 URL을 넣습니다.
                                .size(Size(256, 256)) // 64dp보다 큰 적절한 픽셀 크기로 지정 (256px 정도면 충분)
                                .crossfade(true) // 부드러운 이미지 전환 효과
                                .build(),
                            contentDescription = "${user.nickname}의 프로필 사진",
                            placeholder = painterResource(id = R.drawable.ic_profile_with_background),
                            error = painterResource(id = R.drawable.ic_profile_with_background),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (user.nickname.length > 6) "${user.nickname.take(6)}.." else user.nickname,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        // ✅ 수정된 부분: 태그가 8글자 넘으면 말줄임표 처리
                        Text(
                            text = if (user.motivationTag.length > 6) "#${user.motivationTag.take(6)}.." else "#${user.motivationTag}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_antenna),
                    contentDescription = "No Live",
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "진행중인 라이브가 없어요",
                    color = MORUTheme.colors.mediumGray,
                    style = MORUTheme.typography.desc_M_16
                )
            }
        }
    }
}

// [수정] Preview가 정상적으로 보이도록 샘플 데이터 추가
@Preview(showBackground = true, name = "라이브 있는 경우")
@Composable
private fun MoruLiveSectionPreview() {
    val sampleUsers = listOf(
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            "운동하는 제니--------------",
            "#오운완",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956"
        ),
        LiveUserInfo("3fa85f64-5717-4562-b3fc-2c963f66afa61", "개발자 모루", "#TIL", null),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa62",
            "요가마스터",
            "#요가",
            "https://images.unsplash.com/photo-1552058544-f2b08422138a"
        ),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa63",
            "개발왕",
            "#코딩",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
        ),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa64",
            "요리왕 준",
            "#집밥",
            "https://images.unsplash.com/photo-1541533267753-bab141444692"
        ),
    )

    MoruLiveSection(
        liveUsers = sampleUsers,
        onUserClick = {},
        onTitleClick = {}
    )
}

@Preview(showBackground = true, name = "라이브 없는 경우")
@Composable
private fun MoruLiveSectionEmptyPreview() {
    MoruLiveSection(
        liveUsers = emptyList(),
        onUserClick = {},
        onTitleClick = {}
    )
}