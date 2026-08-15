package com.konkuk.moru.presentation.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.LocalMoruColorsProvider
import com.konkuk.moru.ui.theme.LocalMoruTypographyProvider
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun HomeOnboardingScreen(
    modifier: Modifier,
    onNextClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val colors = LocalMoruColorsProvider.current
    val texts = LocalMoruTypographyProvider.current


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.black50Oopacity),
    ) {
        // x 버튼
        Icon(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = "닫기",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 45.dp, end = 17.dp)
                .size(14.dp)
                .clickable(onClick = onCloseClick)
        )
        // 텍스트와 버튼
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "모루의\n기능을 알아볼까요?",
                style = typography.title_B_24,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .width(114.6.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.5f)) //버튼 배경색
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(onClick = onNextClick),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "다음으로",
                        // bold 16없어서 자체적으로 bold 줌
                        style = typography.desc_M_16.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.next_arrow),
                        contentDescription = "다음 화살표",
                        tint = Color.White,
                        modifier = Modifier
                            .width(9.6.dp)
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun HomeOnboardingScreenPreview() {
    HomeOnboardingScreen(
        modifier = Modifier,
        onNextClick = {},
        onCloseClick = {}
    )
}