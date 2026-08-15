package com.konkuk.moru.presentation.routinefocus.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.graphics.createBitmap
import com.konkuk.moru.R
import com.konkuk.moru.presentation.routinefeed.data.AppDto
import com.konkuk.moru.core.util.HomeAppUtils
import com.konkuk.moru.core.util.HomeAppLaunchUtils
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.MORUTheme.colors

// Drawable을 Bitmap으로 변환하는 함수
private fun drawableToBitmap(drawable: android.graphics.drawable.Drawable): android.graphics.Bitmap {
    return when (drawable) {
        is android.graphics.drawable.BitmapDrawable -> drawable.bitmap
        is android.graphics.drawable.AdaptiveIconDrawable -> {
            val bmp = createBitmap(
                drawable.intrinsicWidth.coerceAtLeast(1),
                drawable.intrinsicHeight.coerceAtLeast(1)
            )
            val canvas = android.graphics.Canvas(bmp)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bmp
        }
        else -> {
            val bmp = createBitmap(
                drawable.intrinsicWidth.coerceAtLeast(1),
                drawable.intrinsicHeight.coerceAtLeast(1)
            )
            val canvas = android.graphics.Canvas(bmp)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bmp
        }
    }
}



@Composable
fun ScreenBlockOverlay(
    selectedApps: List<AppDto> = emptyList(),
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    // 화면 방향에 따른 위치와 크기 조정
    val isLandscape = LocalConfiguration.current.screenWidthDp > LocalConfiguration.current.screenHeightDp
    
    // 어두운 회색 배경에 연두색 테두리 말풍선
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(1000f)
            .background(colors.black50Oopacity)
            .clickable { 
                onDismiss()
            }
    ) {
        // 앱 아이콘들을 하단에 배치 (집중 화면과 동일한 위치)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .offset(y = if (isLandscape) (-52).dp else (-133).dp) // 화면 방향에 따라 위치 조정 (가로모드: 사용앱 팝업과 동일한 높이)
                .zIndex(1001f), // 오버레이보다 위에 표시
            horizontalAlignment = Alignment.Start
        ) {
            // 말풍선을 앱 아이콘들 바로 위에 배치
            Box(
                modifier = Modifier
                    .offset(y = 10.dp) // 말풍선을 앱 아이콘 바로 위에 배치
                    .padding(horizontal = 8.dp) // 좌우 여백을 더 좁게
            ) {
                DarkBalloonWithTail(
                    text = "설정한 앱만 사용할 수 있어요!",
                    balloonWidth = 200.dp,
                    balloonHeight = 40.dp
                )
            }
            
            // 앱 아이콘들 (화면 방향에 따라 크기 조정)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isLandscape) 60.dp else 88.dp) // 가로모드: 60dp, 세로모드: 88dp
                    .background(Color.Transparent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 16.dp, 
                            vertical = if (isLandscape) 12.dp else 20.dp // 가로모드: 12dp, 세로모드: 20dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(if (isLandscape) 14.dp else 14.dp), // 가로모드와 세로모드 동일한 간격
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    android.util.Log.d("ScreenBlockOverlay", "📱 selectedApps 개수: ${selectedApps.size}")
                    selectedApps.forEach { app ->
                        AppIconItem(
                            app = app,
                            isLandscape = isLandscape,
                            onAppClick = { 
                                // 앱 실행
                                android.util.Log.d("ScreenBlockOverlay", "🚀 앱 실행 시도")
                                HomeAppLaunchUtils.launchApp(context, app.packageName, "ScreenBlockOverlay")
                            }
                        )
                    }
                    // 기본 아이콘들 (선택된 앱이 3개 미만인 경우)
                    repeat(3 - selectedApps.size) {
                        AppIconItem(isLandscape = isLandscape)
                    }
                }
            }
        }
    }
}

@Composable
fun DarkBalloonWithTail(
    text: String,
    balloonWidth: Dp = 280.dp,
    balloonHeight: Dp = 60.dp
) {
    val density = LocalDensity.current
    val colors = MORUTheme.colors
    val borderColor = colors.limeGreen // limeGreen 테두리
    val backgroundColor = Color(0xFF3C3C3C) // 어두운 회색 배경
    val textColor = colors.limeGreen // limeGreen 텍스트
    val strokeWidthDp = 2.dp
    val tailHeight = 12.dp
    val tailWidth = 16.dp

    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Canvas(
            modifier = Modifier.size(width = balloonWidth, height = balloonHeight + tailHeight)
        ) {
            val strokePx = with(density) { strokeWidthDp.toPx() }
            val cornerRadius = with(density) { 10.dp.toPx() }
            val tailHeightPx = with(density) { tailHeight.toPx() }
            val tailWidthPx = with(density) { tailWidth.toPx() }

            val balloonBottom = size.height - tailHeightPx
            val tailTipX = size.width * 0.15f // 더 왼쪽으로 꼬리 위치 (15% 지점)
            val tailStartX = tailTipX - tailWidthPx / 2
            val tailEndX = tailTipX + tailWidthPx / 2

            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        0f, 0f,
                        size.width, balloonBottom,
                        CornerRadius(cornerRadius)
                    )
                )
                moveTo(tailStartX, balloonBottom)
                lineTo(tailTipX, size.height)
                lineTo(tailEndX, balloonBottom)
                close()
            }

            // 테두리를 먼저 그린 뒤 → 안쪽을 채운다
            drawPath(path, borderColor, style = Stroke(width = strokePx))
            drawPath(path, backgroundColor)
        }

        Box(
            modifier = Modifier
                .size(balloonWidth, balloonHeight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AppIconItem(
    app: AppDto? = null, 
    isLandscape: Boolean = false,
    onAppClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    // 화면 방향에 따라 아이콘 크기 조정 (사용앱 팝업과 동일하게)
    val iconSize = if (isLandscape) 36.dp else 48.dp // 가로모드: 36dp, 세로모드: 48dp
    
    // 실제 앱 정보가 있으면 실제 앱 아이콘을 표시하고, 없으면 기본 아이콘 표시
    if (app != null) {
        // 실제 앱 아이콘을 가져오는 로직
        val appIcon = remember(app.packageName) {
            HomeAppUtils.getAppIcon(context, app.packageName)
        }
        
        if (appIcon != null) {
            // 실제 앱 아이콘 표시
            Image(
                bitmap = appIcon,
                contentDescription = "${app.name} 아이콘",
                modifier = Modifier
                    .size(iconSize)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onAppClick?.invoke() }
            )
        } else {
            // 앱 아이콘을 가져올 수 없으면 기본 아이콘 표시
            Image(
                painter = painterResource(id = R.drawable.ic_default),
                contentDescription = "${app.name} 아이콘",
                modifier = Modifier
                    .size(iconSize)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onAppClick?.invoke() }
            )
        }
    } else {
        // 기본 아이콘 (선택된 앱이 3개 미만인 경우)
        Image(
            painter = painterResource(id = R.drawable.ic_default),
            contentDescription = "앱 아이콘",
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(6.dp))
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun ScreenBlockOverlayPreview() {
    // 더미 앱 데이터 생성
    val dummyApps = listOf(
        AppDto(
            name = "YouTube",
            packageName = "com.google.android.youtube"
        ),
        AppDto(
            name = "Instagram",
            packageName = "com.instagram.android"
        ),
        AppDto(
            name = "KakaoTalk",
            packageName = "com.kakao.talk"
        )
    )
    
    ScreenBlockOverlay(selectedApps = dummyApps)
}
