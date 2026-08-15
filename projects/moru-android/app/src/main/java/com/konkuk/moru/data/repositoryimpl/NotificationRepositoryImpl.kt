package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.mapper.toDomain
import com.konkuk.moru.data.model.NotificationCursor
import com.konkuk.moru.data.model.NotificationPage
import com.konkuk.moru.data.service.NotificationService
import com.konkuk.moru.domain.repository.NotificationRepository
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import javax.inject.Inject

// 서버가 예시로 주는 포맷: 2025-08-11T17:30:59.799637 (오프셋 없음, 소수 6자리 고정)
private val CURSOR_OUT_FORMATTER: DateTimeFormatter =
    DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .appendFraction(ChronoField.NANO_OF_SECOND, 6, 6, true) // 정확히 6자리
        .toFormatter()

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationService
) : NotificationRepository {

    override suspend fun fetchNotifications(
        cursor: NotificationCursor?,
        size: Int
    ): NotificationPage {
        // Instant -> UTC OffsetDateTime로 변환 후, 서버 포맷(오프셋 없음/마이크로초 6자리)으로 출력
        val createdAtParam = cursor?.createdAt
            ?.atOffset(ZoneOffset.UTC)
            ?.format(CURSOR_OUT_FORMATTER) // 예: 2025-08-11T17:30:59.799637

        val res = api.getNotifications(
            cursorCreatedAt = createdAtParam,
            lastNotificationId = cursor?.id,
            size = size
        )
        return res.toDomain()
    }

    override suspend fun getUnreadCount(): Int {
        return api.getUnreadCount()
    }

    override suspend fun deleteNotification(notificationId: String) {
        api.deleteNotification(notificationId)
    }
    
}