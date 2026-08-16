/*
package com.konkuk.moru.presentation.routinefeed.component.notification


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.konkuk.moru.presentation.routinefeed.data.Notification


@Composable
fun NotificationGroup(
    title: String,
    notifications: List<Notification>,
    isScreenExpanded: Boolean, // 화면 전체가 확장되었는지 여부
    initiallyVisibleCount: Int = 3
) {
    // isScreenExpanded 값에 따라 보여줄 아이템 결정
    val itemsToShow = if (isScreenExpanded) notifications else notifications.take(initiallyVisibleCount)

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        itemsToShow.forEach { notification ->
            NotificationRow(notification = notification, onProfileClick = {})
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }

        // 그룹 내 '더보기' 버튼 제거
    }
}*/
