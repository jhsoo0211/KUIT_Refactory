package com.konkuk.moru.presentation.routinefocus.component

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.presentation.routinefeed.data.AppDto

@Composable
fun AppIcon(
    app: AppDto,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // 실제 앱 아이콘을 가져오는 로직
    val appIcon = remember(app.packageName) {
        try {
            val packageManager = context.packageManager
            
            // 패키지가 설치되어 있는지 먼저 확인
            try {
                packageManager.getPackageInfo(app.packageName, 0)
                android.util.Log.d("AppIcon", "📦 패키지 정보 확인 완료")
            } catch (e: Exception) {
                android.util.Log.e("AppIcon", "❌ 패키지 정보 확인 실패: exception=${e.javaClass.simpleName}")
                throw e
            }
            
            val appInfo = packageManager.getApplicationInfo(app.packageName, 0)
            android.util.Log.d("AppIcon", "✅ ApplicationInfo 로드 성공")
            
            val drawable = packageManager.getApplicationIcon(appInfo)
            android.util.Log.d("AppIcon", "✅ Drawable 로드 성공: ${drawable.intrinsicWidth}x${drawable.intrinsicHeight}")
            
            val bitmap = drawableToBitmap(drawable)
            android.util.Log.d("AppIcon", "✅ Bitmap 변환 성공: ${bitmap.width}x${bitmap.height}")
            
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            android.util.Log.e("AppIcon", "❌ 앱 아이콘 로딩 실패: exception=${e.javaClass.simpleName}")
            
            // 대안적인 방법: 앱 이름으로 검색
            try {
                android.util.Log.d("AppIcon", "🔄 대안 아이콘 검색 시작")
                val fallbackPackageManager = context.packageManager
                val installedApps = fallbackPackageManager.getInstalledApplications(0)
                val matchingApp = installedApps.find { appInfo ->
                    val label = appInfo.loadLabel(fallbackPackageManager).toString().lowercase()
                    val packageName = appInfo.packageName.lowercase()
                    val searchName = app.name.lowercase()
                    
                    // 정확한 매칭
                    label == searchName || packageName == searchName ||
                    // 부분 매칭
                    label.contains(searchName) || packageName.contains(searchName) ||
                    // 한국어 앱명 매칭
                    when (searchName) {
                        "카카오톡" -> label.contains("카카오") || packageName.contains("kakao")
                        "네이버" -> label.contains("네이버") || packageName.contains("naver")
                        "인스타그램" -> label.contains("인스타") || packageName.contains("instagram")
                        "유튜브" -> label.contains("유튜브") || packageName.contains("youtube")
                        else -> false
                    }
                }
                
                if (matchingApp != null) {
                    android.util.Log.d("AppIcon", "✅ 대안 아이콘 검색 성공")
                    val drawable = fallbackPackageManager.getApplicationIcon(matchingApp)
                    val bitmap = drawableToBitmap(drawable)
                    bitmap.asImageBitmap()
                } else {
                    android.util.Log.d("AppIcon", "❌ 대안 아이콘 검색 결과 없음")
                    null
                }
            } catch (e2: Exception) {
                android.util.Log.e("AppIcon", "❌ 대안 아이콘 검색 실패: exception=${e2.javaClass.simpleName}")
                null
            }
        }
    }
    
    if (appIcon != null) {
        Image(
            bitmap = appIcon,
            contentDescription = app.name,
            modifier = modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                    // 앱 실행
                    launchApp(context, app.packageName)
                }
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_default),
            contentDescription = app.name,
            modifier = modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                    // 앱이 설치되어 있지 않을 때 메시지 표시
                    showAppNotInstalledMessage(context, app.name)
                }
        )
    }
}

// Drawable을 Bitmap으로 변환하는 헬퍼 함수
private fun drawableToBitmap(drawable: Drawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

// 앱을 실행하는 헬퍼 함수
private fun launchApp(context: Context, packageName: String) {
    try {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    } catch (e: Exception) {
        // 앱 실행 실패 시 로그 출력
        android.util.Log.e("AppIcon", "앱 실행 실패: exception=${e.javaClass.simpleName}")
    }
}

// 앱이 설치되어 있지 않을 때 메시지를 표시하는 헬퍼 함수
private fun showAppNotInstalledMessage(context: Context, appName: String) {
    try {
        // Toast 메시지로 간단한 알림
        android.widget.Toast.makeText(
            context,
            "$appName 앱이 설치되어 있지 않습니다.",
            android.widget.Toast.LENGTH_SHORT
        ).show()
        
    } catch (e: Exception) {
        android.util.Log.e("AppIcon", "메시지 표시 실패: exception=${e.javaClass.simpleName}")
    }
}
