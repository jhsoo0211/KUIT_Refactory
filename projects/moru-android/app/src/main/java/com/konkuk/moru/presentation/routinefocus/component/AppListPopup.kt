package com.konkuk.moru.presentation.routinefocus.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.konkuk.moru.R
import com.konkuk.moru.presentation.routinefeed.data.AppDto
import com.konkuk.moru.presentation.routinefocus.viewmodel.RoutineFocusViewModel
import com.konkuk.moru.core.util.HomeAppUtils
import com.konkuk.moru.core.util.HomeAppLaunchUtils

@Composable
fun AppListPopup(
    selectedApps: List<AppDto>,
    focusViewModel: RoutineFocusViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() }
        ) {
            // 메인 팝업 컨테이너
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFFFFD700), // 노란색 테두리
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 제목
                Text(
                    text = "사용 가능한 앱",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                
                // 앱 아이콘들
                if (selectedApps.isNotEmpty()) {
                    selectedApps.forEachIndexed { index, app ->
                        AppIconItem(
                            app = app,
                            context = context,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                } else {
                    Text(
                        text = "사용 가능한 앱이 없습니다",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                // 닫기 버튼
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFFFD700),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onDismiss() }
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "닫기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun AppIconItem(
    app: AppDto,
    context: Context,
    modifier: Modifier = Modifier
) {
    // 실제 앱 아이콘을 가져오는 로직
    val appIcon = remember(app.packageName) {
        HomeAppUtils.getAppIcon(context, app.packageName)
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                // 앱 실행
                HomeAppLaunchUtils.launchApp(context, app.packageName, "AppListPopup")
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 앱 아이콘
        if (appIcon != null) {
            Image(
                bitmap = appIcon,
                contentDescription = app.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_default),
                contentDescription = app.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // 앱 이름
        Text(
            text = app.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 실행 버튼
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFF4CAF50), // 초록색
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "실행",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}





