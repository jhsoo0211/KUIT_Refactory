package com.konkuk.moru.presentation.routinefocus.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.konkuk.moru.presentation.routinefeed.data.AppDto
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun FocusOnboardingPopup(
    selectedApps: List<AppDto>,
    onAppClick: (AppDto) -> Unit,
    onOutsideClick: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000).copy(alpha = 0.5f))
            .clickable { onOutsideClick() }
    ) {
        // 메시지 말풍선 (반응형 위치)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = screenHeight * 0.15f) // 화면 높이의 15% 위
                .background(
                    color = colors.black,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .clickable(enabled = false) { /* 말풍선 클릭 방지 */ },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 메시지
            Text(
                text = "설정한 앱만 사용할 수 있어요!",
                style = typography.title_B_12.copy(fontWeight = FontWeight.SemiBold),
                color = colors.black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 사용앱 아이콘들 (점선 테두리로 강조)
            Row(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = colors.red.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                android.util.Log.d("FocusOnboardingPopup", "📱 selectedApps 개수: ${selectedApps.size}")
                selectedApps.forEach { app ->
                    AppIcon(
                        app = app,
                        onClick = { 
                            android.util.Log.d("FocusOnboardingPopup", "🚀 앱 클릭 처리")
                            onAppClick(app)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppIcon(
    app: AppDto,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 앱 아이콘
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.lightGray),
            contentAlignment = Alignment.Center
        ) {
            // 실제 앱에서는 앱 아이콘을 가져와야 함
            // 여기서는 임시로 기본 아이콘 사용
            Text(
                text = app.name.take(1),
                style = typography.title_B_14,
                color = colors.black
            )
        }
    }
}
