package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.konkuk.moru.R
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography


@Composable
fun ActMyInfo(
    profileImage: String? = null,
    routineCount: Int = 0,
    followerCount: Int = 0,
    followingCount: Int = 0,
    userName: String = "사용자명",
    routinePace: String = "미정",
    progress: Float = 0.1f,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .background(Color(0xFFFFFFFF))
            .height(214.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(126.dp)
        ) {
            RoutinePaceCard(userName, routinePace, progress, modifier = Modifier.weight(1f), navController = navController)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(96.dp)
                    .padding(end = 16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(color = colors.veryLightGray)
                ) {
                    val cleaned = profileImage?.trim()

                    if (cleaned.isNullOrBlank()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_profile_basic),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(45.dp)
                        )
                    } else {
                        SubcomposeAsyncImage(
                            model = cleaned,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = {
                                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                            },
                            success = {
                                SubcomposeAsyncImageContent() // 정상 렌더
                            },
                            error = {
                                // 에러 시 기본 이미지 표시
                                Image(
                                    painter = painterResource(R.drawable.ic_profile_basic),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(80.dp)
                        .height(30.dp)
                        .background(color = colors.paleLime, shape = RoundedCornerShape(100.dp))
                        .clickable { navController.navigate(Route.ActProfile.route) }
                ){
                    Text(
                        text = "프로필 수정",
                        style = typography.desc_M_12,
                        color = colors.oliveGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 59.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = routineCount.toString(), color = colors.black, style = typography.body_SB_16)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "루틴", color = colors.mediumGray, style = typography.time_R_12)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = followerCount.toString(), color = colors.black, style = typography.body_SB_16)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "팔로워", color = colors.mediumGray, style = typography.time_R_12)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = followingCount.toString(), color = colors.black, style = typography.body_SB_16)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "팔로잉", color = colors.mediumGray, style = typography.time_R_12)
            }
        }
    }
}
