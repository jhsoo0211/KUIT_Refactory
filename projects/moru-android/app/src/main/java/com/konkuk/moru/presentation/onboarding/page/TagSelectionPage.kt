package com.konkuk.moru.presentation.onboarding.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.konkuk.moru.R
import com.konkuk.moru.core.component.TopBarLogoWithTitle
import com.konkuk.moru.core.component.button.MoruButtonTypeA
import com.konkuk.moru.presentation.onboarding.OnboardingViewModel
import com.konkuk.moru.presentation.onboarding.component.TagItem
import com.konkuk.moru.presentation.onboarding.model.OnboardingTags
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

data class SelectableTag(val label: String) {
    var isSelected by mutableStateOf(false)
}

@Composable
fun TagSelectionPage(
    onNext: () -> Unit,
//    viewModel: OnboardingViewModel? = null
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    //val isPreview = viewModel == null // ✅ 프리뷰 모드 구분

    val situationTags = remember {
        mutableStateListOf<SelectableTag>().apply {
            OnboardingTags.SITUATION.forEach { def -> add(SelectableTag(label = def.label)) }
        }
    }
    val activityTags = remember {
        mutableStateListOf<SelectableTag>().apply {
            OnboardingTags.ACTIVITY.forEach { def -> add(SelectableTag(label = def.label)) }
        }
    }

    val isButtonEnabled = situationTags.any { it.isSelected } && activityTags.any { it.isSelected }

    LaunchedEffect(Unit) {
        viewModel.prefetchTags()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.charcoalBlack)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopBarLogoWithTitle()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 10.dp, top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text("관심태그 설정", style = typography.body_SB_24)
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        "어떤 상황에 주로 사용하시나요?",
                        style = typography.desc_M_14,
                        color = colors.mediumGray
                    )
                    Spacer(modifier = Modifier.height(33.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(152.dp),
                        horizontalArrangement = Arrangement.spacedBy(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        userScrollEnabled = false
                    ) {
                        items(situationTags) { tag ->
                            TagItem(tag.label, tag.isSelected) {
                                tag.isSelected = !tag.isSelected
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(33.dp))
                    Text("어떤 활동을 주로 하시나요?", style = typography.desc_M_14, color = colors.mediumGray)
                    Spacer(modifier = Modifier.height(33.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(152.dp),
                        horizontalArrangement = Arrangement.spacedBy(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        userScrollEnabled = false
                    ) {
                        items(activityTags) { tag ->
                            TagItem(tag.label, tag.isSelected) {
                                tag.isSelected = !tag.isSelected
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_onboarding_statusbar3),
                        contentDescription = "status bar",
                        modifier = Modifier.width(134.dp)
                    )
                    Spacer(modifier = Modifier.height(35.dp))
                    MoruButtonTypeA(text = "다음", enabled = isButtonEnabled) {
                        val selectedLabels = (situationTags + activityTags)
                            .filter { it.isSelected }
                            .map { it.label } // "#라벨"

                        viewModel.submitFavoriteTagsByLabels(selectedLabels)
                        viewModel.updateTags(selectedLabels)
                        onNext()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagSelectionPagePreview() {
    TagSelectionPage(
        onNext = {},
    )
}