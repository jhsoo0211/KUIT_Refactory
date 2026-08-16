package com.konkuk.moru.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import com.konkuk.moru.presentation.routinefeed.data.AppDto

/**
 * 앱 관련 유틸리티 함수들
 */
object HomeAppUtils {
    
    /**
     * 디바이스에 설치된 모든 사용자 앱을 가져옵니다.
     * 시스템 앱은 제외하고 사용자가 설치한 앱만 반환합니다.
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun getInstalledUserApps(context: Context): List<AppDto> {
        return try {
            val packageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            
            installedApps
                .filter { appInfo ->
                    // 시스템 앱 제외 (사용자가 설치한 앱만)
                    (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
                }
                .filter { appInfo ->
                    // LAUNCHER 액티비티가 있는 앱만 포함 (실행 가능한 앱)
                    packageManager.getLaunchIntentForPackage(appInfo.packageName) != null
                }
                .mapNotNull { appInfo ->
                    try {
                        val label = packageManager.getApplicationLabel(appInfo).toString()
                        AppDto(
                            name = label,
                            packageName = appInfo.packageName
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                .sortedBy { it.name.lowercase() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 디바이스에 설치된 모든 앱을 가져옵니다 (시스템 앱 포함).
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun getAllInstalledApps(context: Context): List<AppDto> {
        return try {
            val packageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            
            installedApps
                .filter { appInfo ->
                    // LAUNCHER 액티비티가 있는 앱만 포함 (실행 가능한 앱)
                    packageManager.getLaunchIntentForPackage(appInfo.packageName) != null
                }
                .mapNotNull { appInfo ->
                    try {
                        val label = packageManager.getApplicationLabel(appInfo).toString()
                        AppDto(
                            name = label,
                            packageName = appInfo.packageName
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                .sortedBy { it.name.lowercase() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 특정 카테고리의 앱들만 필터링하여 가져옵니다.
     * 소셜 미디어, 메신저, 브라우저 등 자주 사용되는 앱들을 포함합니다.
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun getPopularApps(context: Context): List<AppDto> {
        return try {
            val packageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            
            val popularAppKeywords = listOf(
                // 소셜 미디어
                "kakao", "naver", "instagram", "youtube", "facebook", "twitter", "tiktok", "snapchat",
                // 메신저
                "whatsapp", "telegram", "line", "wechat", "discord", "slack",
                // 브라우저
                "chrome", "firefox", "samsung internet", "edge",
                // 비디오 통화
                "zoom", "teams", "meet",
                // 한국어 앱명
                "카카오", "네이버", "인스타", "유튜브", "페이스북", "트위터", "틱톡", "스냅챗",
                "왓츠앱", "텔레그램", "라인", "위챗", "디스코드", "슬랙", "줌", "팀즈", "미트",
                "크롬", "파이어폭스", "삼성", "엣지"
            )
            
            installedApps
                .filter { appInfo ->
                    val packageName = appInfo.packageName.lowercase()
                    val label = packageManager.getApplicationLabel(appInfo).toString().lowercase()
                    
                    // 시스템 앱 제외
                    (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    // 인기 앱 키워드와 매칭
                    popularAppKeywords.any { keyword ->
                        packageName.contains(keyword) || label.contains(keyword)
                    }
                }
                .mapNotNull { appInfo ->
                    try {
                        val label = packageManager.getApplicationLabel(appInfo).toString()
                        AppDto(
                            name = label,
                            packageName = appInfo.packageName
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                .sortedBy { it.name.lowercase() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Drawable을 ImageBitmap으로 변환합니다.
     */
    fun drawableToImageBitmap(drawable: Drawable): ImageBitmap {
        return when (drawable) {
            is android.graphics.drawable.BitmapDrawable -> drawable.bitmap.asImageBitmap()
            is android.graphics.drawable.AdaptiveIconDrawable -> {
                val bmp = createBitmap(
                    drawable.intrinsicWidth.coerceAtLeast(1),
                    drawable.intrinsicHeight.coerceAtLeast(1)
                )
                val canvas = Canvas(bmp)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bmp.asImageBitmap()
            }
            else -> {
                val bmp = createBitmap(
                    drawable.intrinsicWidth.coerceAtLeast(1),
                    drawable.intrinsicHeight.coerceAtLeast(1)
                )
                val canvas = Canvas(bmp)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bmp.asImageBitmap()
            }
        }
    }
    
    /**
     * 앱 아이콘을 ImageBitmap으로 가져옵니다.
     */
    fun getAppIcon(context: Context, packageName: String): ImageBitmap? {
        return try {
            val packageManager = context.packageManager
            val drawable = packageManager.getApplicationIcon(packageName)
            drawableToImageBitmap(drawable)
        } catch (e: Exception) {
            null
        }
    }
}
