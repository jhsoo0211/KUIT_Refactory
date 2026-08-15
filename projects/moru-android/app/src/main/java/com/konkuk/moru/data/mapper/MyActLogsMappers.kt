package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.*
import com.konkuk.moru.domain.model.*
import kotlinx.serialization.json.*
import java.time.Duration
import java.time.LocalDate

private fun parseTotalSeconds(el: JsonElement?): Long {
    if (el == null) return 0L
    return when (el) {
        is JsonPrimitive -> if (el.isString) runCatching { Duration.parse(el.content).seconds }.getOrDefault(0L) else 0L
        is JsonObject    -> el["seconds"]?.jsonPrimitive?.longOrNull ?: 0L
        else             -> 0L
    }
}

fun MyActLogItemDto.toDomain(today: LocalDate = LocalDate.now()): MyActRecord =
    MyActRecord(
        id = logId,
        title = routineTitle,
        tags = tags,
        isCompleted = isCompleted,
        imageUrl = imageUrl ?: "",
        durationSec = parseTotalSeconds(totalTime),
        startedAt = today
    )

fun MyActLogsCursorDto.toDomain(): MyActRecordCursor =
    MyActRecordCursor(createdAt = createdAt, logId = logId)

fun MyActLogsPageResponse.toDomain(): MyActRecordPage =
    MyActRecordPage(
        items = content.map { it.toDomain() },
        hasNext = hasNext,
        nextCursor = nextCursor?.toDomain()
    )
