package com.konkuk.moru.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.konkuk.moru.presentation.onboarding.component.OnboardingPage

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState()
    val currentPage by viewModel.currentPage.collectAsState()

    // 온보딩 완료 시 콜백 호출
    val isOnboardingComplete by viewModel.isOnboardingComplete.collectAsState()
    LaunchedEffect(isOnboardingComplete) {
        if (isOnboardingComplete) {
            onFinish()
        }
    }

    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            count = OnboardingViewModel.LAST_PAGE_INDEX + 1,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            userScrollEnabled = false // 스와이프 금지, 버튼으로만 이동
        ) { page ->
            OnboardingPage(page = page, onNext = { viewModel.nextPage() })
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    //OnboardingScreen {  }
}