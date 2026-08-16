package com.konkuk.moru.presentation.onboarding.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.core.component.TopBarLogoWithTitle
import com.konkuk.moru.core.component.button.MoruButtonTypeA
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun InfoPage1(onNext: () -> Unit) {
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
                    .background(Color(0xFFFFFFFF))
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_infopage1),
                        contentDescription = "Info Page 1",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop // 꽉 차도록 자르기
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp),
                    ) {
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            text = "관심태그를 추가하세요",
                            style = typography.body_SB_24
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "MORU는 관심태그를  기반으로 루틴을 추천합니다.",
                            style = typography.desc_M_14,
                            color = colors.mediumGray
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = "관심태그를 설정하고 루틴을 추천받아요!",
                            style = typography.desc_M_14,
                            color = colors.mediumGray
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = com.konkuk.moru.R.drawable.ic_onboarding_statusbar2),
                        contentDescription = "status bar",
                        modifier = Modifier.width(134.dp)
                    )
                    Spacer(modifier = Modifier.height(35.dp))
                    MoruButtonTypeA(text = "다음", enabled = true) {onNext()}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoPage1Preview() {
    InfoPage1(){}
}