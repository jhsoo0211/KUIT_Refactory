package com.konkuk.moru.presentation.myactivity.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.presentation.myactivity.component.ActMyInfo
import com.konkuk.moru.presentation.myactivity.component.MyActivityTab
import com.konkuk.moru.presentation.myactivity.component.MyProfileTitle
import com.konkuk.moru.presentation.myactivity.viewmodel.InsightViewModel
import com.konkuk.moru.presentation.myactivity.viewmodel.ActUserViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors

@Composable
fun ActMainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: InsightViewModel = hiltViewModel(),
    viewModelUser: ActUserViewModel = hiltViewModel()
) {
    val nickname by viewModelUser.nickname.collectAsState()
    val routineCount by viewModelUser.routineCount.collectAsState()
    val followerCount by viewModelUser.followerCount.collectAsState()
    val followingCount by viewModelUser.followingCount.collectAsState()
    val profileImage by viewModelUser.profileImageUrl.collectAsState()

    LaunchedEffect(Unit) {
        viewModelUser.loadMe()
    }

    val uiState = viewModel.ui.collectAsState().value

    val routineStatus = when (uiState.routineCompletionRate.toFloat()) {
        in 0f..0.3f -> "잠시 걷는 중"
        in 0.3f..0.7f -> "간헐적 루틴러"
        else -> "루틴 페이스 메이커"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ){
        MyProfileTitle(navController)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 16.dp, end = 16.dp)
                .background(colors.lightGray)
        ){}

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            ActMyInfo(profileImage, routineCount, followerCount, followingCount, nickname ?: "알 수 없음", routineStatus, uiState.routineCompletionRate.toFloat(), navController = navController)
            Spacer(modifier = Modifier.height(24.dp))

            var selectedTab by remember { mutableStateOf(0) }
            MyActivityTab(
                insightData = uiState,
                userName = nickname ?: "알 수 없음",
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}