package com.konkuk.moru.core.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun TopBarLogoWithTitle() { // 회원가입과 온보딩에서 쓰이는 로고와 타이틀 컴포넌트
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF1A1A1A)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_signup_moru_logo),
                contentDescription = "Moru Logo",
                modifier = Modifier
                    .padding(0.dp)
                    .width(30.dp)
                    .height(30.dp)
            )

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = "MORU",
                style = typography.desc_M_20,
                color = colors.paleLime
            )
        }
    }

}

@Preview
@Composable
private fun TopBarLogoWithTitlePreview() {
    TopBarLogoWithTitle()
}