package com.konkuk.moru.data.model

import java.time.Instant

data class NotificationItem(
    val id: String,
    val senderId: String,
    val senderNickname: String,
    val senderProfileImage: String?,
    val message: String,
    val createdAt: Instant?,     // 서버가 주면 채움, 지금은 null일 수 있음
    val relativeTime: String
)

data class NotificationCursor(
    val createdAt: Instant,
    val id: String
)

data class NotificationPage(
    val items: List<NotificationItem>,
    val hasNext: Boolean,
    val nextCursor: NotificationCursor?
)