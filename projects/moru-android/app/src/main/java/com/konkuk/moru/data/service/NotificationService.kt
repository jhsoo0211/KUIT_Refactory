package com.konkuk.moru.data.service

import com.konkuk.moru.presentation.routinefeed.data.NotificationListResponseDto
import retrofit2.Response

import retrofit2.http.DELETE
import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationService {
    @GET("/api/notifications")
    suspend fun getNotifications(
        @Query("cursorCreatedAt") cursorCreatedAt: String? = null,
        @Query("lastNotificationId") lastNotificationId: String? = null,
        @Query("size") size: Int = 20
    ): NotificationListResponseDto


    @GET("/api/notifications/unread-count")
    suspend fun getUnreadCount(): Int

    /// [추가] 개별 알림 삭제 API
    @DELETE("/api/notifications/{notificationId}")
    suspend fun deleteNotification(@Path("notificationId") notificationId: String): Response<Unit>

}