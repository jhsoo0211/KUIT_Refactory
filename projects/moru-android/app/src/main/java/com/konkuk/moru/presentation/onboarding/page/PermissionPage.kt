package com.konkuk.moru.presentation.onboarding.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.konkuk.moru.core.component.button.MoruButtonTypeA
import com.konkuk.moru.presentation.onboarding.component.PermissionItem
import com.konkuk.moru.presentation.onboarding.model.PermissionType
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import com.konkuk.moru.presentation.onboarding.permission.rememberPermissionController

@Composable
fun PermissionPage(
    onNext: () -> Unit,
) {
    // [변경] 내부 로직 삭제 → 컨트롤러에서 다 처리
    val controller = rememberPermissionController()
    val permissions = listOf(
        Triple("푸시 알림 허용", "루틴 실천에 도움되는 알림을 받으세요!", PermissionType.PUSH_NOTIFICATION),
        Triple("시간 알림 허용", "루틴 시간에 맞춰 알림을 받으세요!", PermissionType.SCHEDULE_EXACT_ALARM),
        Triple("다른 앱 위 표시 허용", "루틴 실천 시, 집중하기 위해 필요해요!", PermissionType.OVERLAY),
        Triple("방해 금지 모드 제어 허용", "루틴 실천 중 불필요한 알림을 막아요!", PermissionType.DO_NOT_DISTURB)
    )

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
                    .padding(bottom = 10.dp, top = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(text = "앱 이용을 위해", style = typography.body_SB_24)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "아래 접근 권한 허용이 필요해요", style = typography.body_SB_24)
                    Spacer(modifier = Modifier.height(60.dp))

                    permissions.forEach { (title, desc, type) ->
                        PermissionItem(
                            title = title,
                            description = desc,
                            type = type,
                            // [변경] 컨트롤러 상태 사용
                            isGranted = controller.states[type] == true,
                            onClick = { controller.onClick(type) } // [변경] 컨트롤러 콜백
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_onboarding_statusbar5),
                        contentDescription = "status bar",
                        modifier = Modifier.width(134.dp)
                    )
                    Spacer(modifier = Modifier.height(35.dp))
                    MoruButtonTypeA(
                        text = "다음",
                        enabled = controller.allGranted, // [변경]
                        onClick = onNext
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PermissionPagePreview() {
    PermissionPage(onNext = {})
}