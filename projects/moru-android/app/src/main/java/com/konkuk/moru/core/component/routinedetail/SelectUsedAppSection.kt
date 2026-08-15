package com.konkuk.moru.core.component.routinedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.routinedetail.appdisplay.AddAppBox
import com.konkuk.moru.core.component.routinedetail.appdisplay.SelectedAppNoText
import com.konkuk.moru.data.model.UsedAppInRoutine
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun SelectUsedAppSection(
    selectedAppList: List<UsedAppInRoutine> = emptyList(),
    isEditMode: Boolean = true,
    onRemove: (UsedAppInRoutine) -> Unit = {},
    onAddApp: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(bottom = 30.dp)
    ) {
        Text(
            text = "사용앱",
            style = typography.title_B_20,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            contentPadding = PaddingValues(start = 11.dp),
        ) {
            items(selectedAppList) { app ->
                SelectedAppNoText(
                    appIcon = app.appIcon,
                    isRemovable = isEditMode
                ) { onRemove(app) }
            }

            if (selectedAppList.size < 4 && isEditMode) {
                item {
                    AddAppBox {
//                        coroutineScope.launch {
//                            isBottomSheetOpen = true
//                        }
                        onAddApp()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SelectUsedAppSectionPreview() {
    SelectUsedAppSection(){}
}