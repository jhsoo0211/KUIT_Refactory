package com.konkuk.moru.core.component.routinedetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.routinedetail.appdisplay.SelectedAppInBottomSheet
import com.konkuk.moru.core.component.routinedetail.appdisplay.UnselectedAppInBottomSheet
import com.konkuk.moru.data.model.UsedAppInRoutine
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraggableAppSearchBottomSheet(
    isVisible: Boolean,
    appList: List<UsedAppInRoutine>,
    selectedAppList: List<UsedAppInRoutine>,
    onAddApp: (UsedAppInRoutine) -> Unit,
    onRemoveApp: (UsedAppInRoutine) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sheetHeight = screenHeight * 0.67f

    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    if (isVisible || sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color(0xFFF1F3F5),
            dragHandle = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .width(30.dp)
                            .height(5.dp)
                            .background(
                                color = colors.bottomSheetHandleGray,
                                RoundedCornerShape(50)
                            )
                    )
                }
            },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            scrimColor = Color.Black.copy(alpha = 0.0f),
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier
                    .height(sheetHeight)
                    .fillMaxWidth()
            ) {
                DraggableAppSearchBottomSheetContent(
                    appList = appList,
                    selectedAppList = selectedAppList,
                    onAddApp = { app ->
                        onAddApp(app)
                    },
                    onRemoveApp = { app ->
                        onRemoveApp(app)
                    }
                )
            }
        }
    }
}

// 내부 컨텐츠만 분리
@Composable
fun DraggableAppSearchBottomSheetContent(
    appList: List<UsedAppInRoutine>,
    selectedAppList: List<UsedAppInRoutine>,
    onAddApp: (UsedAppInRoutine) -> Unit,
    onRemoveApp: (UsedAppInRoutine) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "사용앱",
                style = typography.body_SB_16,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            HorizontalDivider(
                color = colors.lightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (selectedAppList.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .padding(bottom = 6.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.White, shape = RoundedCornerShape(50))
                            .padding(horizontal = 11.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedAppList.size.toString() + "/4",
                            style = typography.desc_M_12,
                            color = colors.mediumGray
                        )
                    }
                }
                if (selectedAppList.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        verticalArrangement = Arrangement.spacedBy(27.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 0.dp, start = 21.dp, end = 25.dp),
                    ) {
                        items(selectedAppList) { app ->
                            SelectedAppInBottomSheet(
                                appIcon = app.appIcon,
                                appName = app.appName
                            ) {
                                if (app in selectedAppList) {
                                    onRemoveApp(app)
                                }
                            }
                        }
                    }
                }
            }


            SearchAppTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .padding(top = 26.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            val filteredApps = remember(searchText, appList, selectedAppList) {
                appList
                    .filter { it.appName.contains(searchText, ignoreCase = true) }
                    .filterNot { sel -> selectedAppList.any { it.packageName == sel.packageName } }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(27.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(bottom = 16.dp, start = 25.dp, end = 25.dp),
            ) {
                items(if (searchText.isBlank()) appList else filteredApps) { app ->
                    UnselectedAppInBottomSheet(
                        appIcon = app.appIcon,
                        appName = app.appName
                    ) {
                        if (selectedAppList.size < 4) {
                            onAddApp(app)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DraggableAppSearchBottomSheetContentPreview() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        DraggableAppSearchBottomSheetContent(
            appList = listOf(
                UsedAppInRoutine("YouTube", ImageBitmap(64, 64), ""),
                UsedAppInRoutine("Instagram", ImageBitmap(64, 64), ""),
                UsedAppInRoutine("Twitter", ImageBitmap(64, 64), ""),
                UsedAppInRoutine("Facebook", ImageBitmap(64, 64), "")
            ),
            selectedAppList = listOf(
                UsedAppInRoutine("WhatsApp", ImageBitmap(64, 64), ""),
                UsedAppInRoutine("Telegram", ImageBitmap(64, 64), "")
            ),
            onAddApp = {},
            onRemoveApp = {}
        )
    }
}