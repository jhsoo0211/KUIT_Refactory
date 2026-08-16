package com.konkuk.moru.presentation.onboarding.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.core.component.TopBarLogoWithTitle
import com.konkuk.moru.core.component.button.MoruButtonStart
import com.konkuk.moru.presentation.onboarding.OnboardingViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun FinalPage(
    onNext: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.charcoalBlack)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            TopBarLogoWithTitle()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 10.dp, top = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "MORU를 사용하면...",
                        style = typography.body_SB_24
                    )
                    Spacer(modifier = Modifier.height(38.dp))
                    Row {
                        Box(
                            modifier = Modifier.size(75.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_onboarding_finalpage1),
                                contentDescription = "icon",
                                modifier = Modifier.width(45.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text(
                                text = "편한 실천",
                                style = typography.body_SB_20
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "루틴을 부담없이 시작하고",
                                style = typography.desc_M_14,
                                color = colors.mediumGray
                            )
                            Text(
                                text = "끝까지 이어갈 수 있도록 도와줄게요!",
                                style = typography.desc_M_14,
                                color = colors.mediumGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row {
                        Box(
                            modifier = Modifier.size(75.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_onboarding_finalpage2),
                                contentDescription = "icon",
                                modifier = Modifier.width(45.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text(
                                text = "동기부여",
                                style = typography.body_SB_20
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "사람들과 루틴 나누고",
                                style = typography.desc_M_14,
                                color = colors.mediumGray
                            )
                            Text(
                                text = "응원하며 힘을 얻어요!",
                                style = typography.desc_M_14,
                                color = colors.mediumGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row {
                        Box(
                            modifier = Modifier.size(75.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_onboarding_finalpage3),
                                contentDescription = "icon",
                                modifier = Modifier.width(45.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text(
                                text = "맞춤 피드백",
                                style = typography.body_SB_20
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "무엇이 잘 되고 무엇이 어려운지,",
                                style = typography.desc_M_14,
                                color = colors.mediumGray
                            )
                            Text(
                                text = "데이터로 알려드려요!",
                                style = typography.desc_M_14,
                                color = colors.mediumGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_onboarding_statusbar7),
                        contentDescription = "status bar",
                        modifier = Modifier.width(134.dp)
                    )
                    Spacer(modifier = Modifier.height(35.dp))
                    MoruButtonStart { onNext() }
                }
            }
        }
    }
}

@Preview
@Composable
fun FinalPagePreview() {
    FinalPage(onNext = {})
}
