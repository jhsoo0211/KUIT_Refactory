package com.konkuk.moru.presentation.myactivity.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActPrivacyPolicyScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("개인정보 처리방침", style = typography.title_B_20, color = colors.black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,          // 배경색
                    titleContentColor = Color.Black,       // 제목 색
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { inner ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(inner)
        ) {
            MyActPrivacyPolicyContent(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            )
        }
    }
}

@Composable
fun MyActPrivacyPolicyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "개인정보 처리방침 (Demo ver)",
            style = typography.title_B_20,
            color = colors.black
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = "현재 앱버전의 애플리케이션은 데모데이 시연용으로 제작된 테스트 서비스입니다.",
            style = typography.desc_M_14,
            color = colors.darkGray
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = "실제 개인정보 수집, 저장, 활용을 하지 않으며, 입력된 정보는 시연 목적 외의 용도로 사용되지 않습니다.",
            style = typography.desc_M_14,
            color = colors.darkGray
        )
        Spacer(Modifier.height(16.dp))

        Text(text = "수집 항목", style = typography.body_SB_16, color = colors.black)
        Spacer(Modifier.height(8.dp))
        MyActBullet("사용자가 입력한 테스트 정보: 닉네임, 이메일, 성별, 생년월일 등")

        Spacer(Modifier.height(16.dp))
        Text(text = "이용 목적", style = typography.body_SB_16, color = colors.black)
        Spacer(Modifier.height(8.dp))
        MyActBullet("시연 및 기능 테스트 용도에 한함")

        Spacer(Modifier.height(16.dp))
        Text(text = "보관 기간", style = typography.body_SB_16, color = colors.black)
        Spacer(Modifier.height(8.dp))
        MyActBullet("데모데이 이후 서버 초기화 예정")
    }
}

@Composable
fun MyActBullet(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp, end = 8.dp)
                .size(6.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(colors.oliveGreen)
        )
        Text(
            text = text,
            style = typography.desc_M_14,
            color = colors.darkGray
        )
    }
}