package com.konkuk.moru.presentation.onboarding.component

import androidx.compose.runtime.Composable
import com.konkuk.moru.presentation.onboarding.OnboardingViewModel
import com.konkuk.moru.presentation.onboarding.page.FinalPage
import com.konkuk.moru.presentation.onboarding.page.InfoPage1
import com.konkuk.moru.presentation.onboarding.page.InfoPage2
import com.konkuk.moru.presentation.onboarding.page.InfoPage3
import com.konkuk.moru.presentation.onboarding.page.PermissionPage
import com.konkuk.moru.presentation.onboarding.page.TagSelectionPage
import com.konkuk.moru.presentation.onboarding.page.UserInfoPage

@Composable
fun OnboardingPage(page: Int, onNext: () -> Unit) {
    when (page) {
        0 -> UserInfoPage(onNext = onNext)
        1 -> InfoPage1(onNext = onNext)
        2 -> TagSelectionPage(onNext = onNext)
        3 -> InfoPage2(onNext = onNext)
        4 -> PermissionPage(onNext = onNext)
        5 -> InfoPage3(onNext = onNext)
        6 -> FinalPage(onNext = onNext)
        else -> throw IllegalArgumentException("Invalid page number: $page")
    }
}