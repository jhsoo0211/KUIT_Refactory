package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.MyActRoutineLogResponse
import com.konkuk.moru.domain.model.MyActRecord
import java.time.Duration
import java.time.LocalDate

private fun parseIsoDurationToSeconds(value: String?): Long =
    runCatching { if (value.isNullOrBlank()) 0L else Duration.parse(value).seconds }
        .getOrDefault(0L)

fun MyActRoutineLogResponse.toDomain(today: LocalDate = LocalDate.now()): MyActRecord =
    MyActRecord(
        id = logId,
        title = routineTitle,
        tags = tags,
        isCompleted = isCompleted,
        imageUrl = imageUrl ?: "",
        durationSec = parseIsoDurationToSeconds(totalTime),
        startedAt = today
    )
