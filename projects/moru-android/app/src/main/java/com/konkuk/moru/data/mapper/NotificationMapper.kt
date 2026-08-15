package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.model.NotificationCursor
import com.konkuk.moru.data.model.NotificationItem
import com.konkuk.moru.data.model.NotificationPage
import com.konkuk.moru.presentation.routinefeed.data.NotificationCursorDto
import com.konkuk.moru.presentation.routinefeed.data.NotificationItemDto
import com.konkuk.moru.presentation.routinefeed.data.NotificationListResponseDto
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

fun NotificationItemDto.toDomain(): NotificationItem =
    NotificationItem(
        id = id,
        senderId = senderId,
        senderNickname = senderNickname,
        senderProfileImage = senderProfileImage,
        message = message,
        createdAt = null,          // per-item createdAt 없으면 null 유지
        relativeTime = relativeTime
    )

// 서버가 내려주는 createdAt 예시:
// - "2025-08-11T17:30:59.799637" (오프셋 없음, 소수 6자리)
// - 혹시 "2025-08-11T17:30:59.799637Z" 같은 변형도 대응
private val CURSOR_IN_FORMATTER = DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
    .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd()
    .optionalStart().appendOffsetId().optionalEnd() // Z, +00:00 등도 허용
    .toFormatter()

fun NotificationCursorDto.toDomain(): NotificationCursor {
    val parsed = CURSOR_IN_FORMATTER.parseBest(
        createdAt,
        { t -> OffsetDateTime.from(t) },
        { t -> LocalDateTime.from(t) }
    )

    val instant: Instant = when (parsed) {
        is OffsetDateTime -> parsed.toInstant()
        is LocalDateTime  -> parsed.toInstant(ZoneOffset.UTC) // 오프셋 없으면 UTC로 간주
        else -> error("Unsupported createdAt: $createdAt")
    }

    return NotificationCursor(
        createdAt = instant,
        id = id
    )
}

fun NotificationListResponseDto.toDomain(): NotificationPage =
    NotificationPage(
        items = content.map { it.toDomain() },
        hasNext = hasNext,
        nextCursor = nextCursor?.toDomain()
    )