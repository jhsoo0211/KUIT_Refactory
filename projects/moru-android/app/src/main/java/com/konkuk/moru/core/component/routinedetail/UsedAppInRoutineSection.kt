package com.konkuk.moru.core.component.routinedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.routinedetail.appdisplay.AddAppBox
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun UsedAppInRoutineSection(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(bottom = 30.dp)
    ) {
        Text(
            "사용앱",
            style = typography.title_B_20,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier.padding(start = 11.dp)) {
            AddAppBox {
                onClick()
                // 이곳을 누르면 기기에 설치된 앱 목록을 불러오고 앱들중 원하는 앱을 선택할 수 있는 바텀시트가 올라옴
            }
        }
    }
}