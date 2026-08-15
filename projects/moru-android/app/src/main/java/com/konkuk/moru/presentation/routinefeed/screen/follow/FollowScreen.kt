package com.konkuk.moru.presentation.routinefeed.screen.follow

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.moru.presentation.routinefeed.component.follow.FollowScreenContent
import com.konkuk.moru.presentation.routinefeed.data.FollowUser
import com.konkuk.moru.presentation.routinefeed.viewmodel.FollowViewModel
import com.konkuk.moru.ui.theme.MORUTheme
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FollowScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    viewModel: FollowViewModel = hiltViewModel(),
    selectedTab: String?
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs = listOf("팔로워", "팔로잉")
    val scope = rememberCoroutineScope()
    val initialPage = if (selectedTab == "following") 1 else 0
    val pagerState = rememberPagerState(initialPage = initialPage) { tabs.size }

    LaunchedEffect(Unit) {
        viewModel.followResult.collect { (userId, isFollowing) ->
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("follow_result_$userId", isFollowing)
        }
    }


    Column(
        modifier = Modifier
            .padding(bottom = 80.dp)
            .background(Color.White)
    ) {
        FollowScreenContent(
            uiState = uiState,
            tabs = tabs,
            pagerState = pagerState,
            scope = scope,
            onBackClick = onBackClick,
            onFollowClick = viewModel::toggleFollow,
            onUserClick = onUserClick
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun FollowScreenPreview() {
    // 1. Preview에서 사용할 가상 데이터와 상태를 만듭니다.
    var uiState by remember {
        mutableStateOf(
            FollowUiState(
                followers = listOf(
                    FollowUser("routine-id-1", "", "Moru_Official", "모루 공식 계정입니다.", true),
                    FollowUser("routine-id-2", "", "Android_Lover", "안드로이드 개발자", false),
                    FollowUser("routine-id-3", "", "Compose_Fan", "컴포즈는 쉬워요!", true),
                ), followings = listOf(
                    FollowUser("routine-id-1", "", "Moru_Official", "모루 공식 계정입니다.", true),
                    FollowUser("routine-id-3", "", "Compose_Fan", "컴포즈는 쉬워요!", true),
                    FollowUser("routine-id-4", "", "Kotlin_User", "코틀린 최고", true),
                    FollowUser("routine-id-5", "Jetpack_Guru", "제트팩 전문가", "코틀린 최고", true),
                )
            )
        )
    }
    val tabs = listOf("팔로워", "팔로잉")
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    MORUTheme {
        // 2. Stateless Composable인 FollowScreenContent를 호출합니다.
        FollowScreenContent(
            uiState = uiState,
            tabs = tabs,
            pagerState = pagerState,
            scope = scope,
            onBackClick = {},
            // 3. Preview 내에서만 동작하는 클릭 로직을 구현합니다.
            onFollowClick = { clickedUser ->
                val isCurrentlyFollowing = clickedUser.isFollowing
                val followers = uiState.followers.toMutableList()
                val followings = uiState.followings.toMutableList()

                if (isCurrentlyFollowing) { // 언팔로우
                    followings.removeIf { it.id == clickedUser.id }
                    val followerIndex = followers.indexOfFirst { it.id == clickedUser.id }
                    if (followerIndex != -1) {
                        followers[followerIndex] =
                            followers[followerIndex].copy(isFollowing = false)
                    }
                } else { // 팔로우
                    if (followings.none { it.id == clickedUser.id }) {
                        followings.add(0, clickedUser.copy(isFollowing = true))
                    }
                    val followerIndex = followers.indexOfFirst { it.id == clickedUser.id }
                    if (followerIndex != -1) {
                        followers[followerIndex] = followers[followerIndex].copy(isFollowing = true)
                    }
                }
                // Preview의 상태를 업데이트하여 UI 변경을 확인합니다.
                uiState = uiState.copy(followers = followers, followings = followings)
            },
        onUserClick = {}

        )
    }
}
