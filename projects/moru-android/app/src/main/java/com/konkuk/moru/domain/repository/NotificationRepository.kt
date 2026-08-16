package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.model.NotificationCursor
import com.konkuk.moru.data.model.NotificationPage

interface NotificationRepository {
    suspend fun fetchNotifications(
        cursor: NotificationCursor? = null,
        size: Int = 20
    ): NotificationPage
    suspend fun getUnreadCount(): Int
    suspend fun deleteNotification(notificationId: String) // [추가]

}