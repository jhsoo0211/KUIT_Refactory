package com.konkuk.moru.presentation.myroutines.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.konkuk.moru.R
import com.konkuk.moru.core.component.routinedetail.appdisplay.AddAppBox
import com.konkuk.moru.data.model.AppInfo
import com.konkuk.moru.ui.theme.MORUTheme

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
fun UsedAppsSection(
    apps: List<AppInfo>,
    isEditMode: Boolean,
    onAddApp: () -> Unit,
    onDeleteApp: (AppInfo) -> Unit
) {
    val context = LocalContext.current
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("사용 앱", style = MORUTheme.typography.title_B_20, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment =Alignment.Top,
        ) {
            items(apps) { app ->
                Box {
                    Column(
                        modifier = Modifier.width(52.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                        // 실제 앱 아이콘을 가져오는 로직
                        val appIcon = remember(app.packageName) {
                            try {
                                val packageManager = context.packageManager
                                val appInfo = packageManager.getApplicationInfo(app.packageName, 0)
                                val drawable = packageManager.getApplicationIcon(appInfo)
                                drawableToBitmap(drawable).asImageBitmap()
                            } catch (e: Exception) {
                                null
                            }
                        }
                        
                        if (appIcon != null) {
                            androidx.compose.foundation.Image(
                                bitmap = appIcon,
                                contentDescription = app.name,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MORUTheme.colors.veryLightGray,
                                        shape = RoundedCornerShape(size = 6.dp)
                                    )
                                    .padding(8.dp)
                            )
                        } else {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.ic_reset),
                                contentDescription = app.name,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MORUTheme.colors.veryLightGray,
                                        shape = RoundedCornerShape(size = 6.dp)
                                    )
                                    .padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            app.name,
                            style = MORUTheme.typography.desc_M_12,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (isEditMode) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Delete App",
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.TopStart)
                                .clip(CircleShape)
                                .background(MORUTheme.colors.lightGray)
                                .clickable { onDeleteApp(app) },
                            tint = MORUTheme.colors.darkGray
                        )
                    }
                }
            }

            if (isEditMode) {
                item {
                    AddAppBox(
                        onClick = onAddApp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "사용 앱 - 보기 모드")
@Composable
private fun UsedAppsSectionPreview_ViewMode() {
    val apps = listOf(
        AppInfo(name = "YouTube", packageName = "com.google.android.youtube"),
        AppInfo(name = "Notion", packageName = "so.notion.id"),
        AppInfo(name = "Spotify", packageName = "com.spotify.music"),
    )
    MORUTheme {
        UsedAppsSection(
            apps = apps,
            isEditMode = false,
            onAddApp = {},
            onDeleteApp = {}
        )
    }
}

@Preview(showBackground = true, name = "사용 앱 - 편집 모드")
@Composable
private fun UsedAppsSectionPreview_EditMode() {
    val apps = listOf(
        AppInfo(name = "YouTube", packageName = "com.google.android.youtube"),
        AppInfo(name = "Keep 메모", packageName = "com.google.android.keep"),
        AppInfo(name = "To Do", packageName = "com.microsoft.todos"),
        AppInfo(name = "Forest", packageName = "cc.forestapp"),
    )
    MORUTheme {
        UsedAppsSection(
            apps = apps,
            isEditMode = true,
            onAddApp = {},
            onDeleteApp = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UsedAppsSectionPreview() {
    val sampleApps = listOf(
        AppInfo("app1", "com.example.app1"),
        AppInfo("app2", "com.example.app2"),
        AppInfo("app3", "com.example.app3"),
        AppInfo("app4", "com.example.app4")
    )
    MORUTheme {
        UsedAppsSection(
            apps = sampleApps,
            isEditMode = true,
            onAddApp = { /* TODO: Add app logic */ },
            onDeleteApp = { /* TODO: Delete app logic */ }
        )
    }
}
