package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.*
import com.konkuk.moru.domain.model.*
import kotlinx.serialization.json.*
import java.time.Duration

private fun parseTotalSeconds(el: JsonElement?): Long {
    if (el == null) return 0L
    return when (el) {
        is JsonPrimitive -> if (el.isString)
            runCatching { Duration.parse(el.content).seconds }.getOrDefault(0L)
        else 0L
        is JsonObject -> el["seconds"]?.jsonPrimitive?.longOrNull ?: 0L
        else -> 0L
    }
}

private fun MyActRecordDetailStepDto.toDomain(): MyActRecordDetailStep =
    MyActRecordDetailStep(
        order = stepOrder,
        name = stepName,
        note = note,
        estimatedSec = parseTotalSeconds(estimatedTime),
        actualSec = parseTotalSeconds(actualTime),
        isCompleted = isCompleted
    )

fun MyActRecordDetailResponse.toDomain(): MyActRecordDetail =
    MyActRecordDetail(
        id = id,
        title = routineTitle,
        isSimple = isSimple,
        isCompleted = isCompleted,
        startedAtIso = startedAt,
        endedAtIso = endedAt,
        totalSec = parseTotalSeconds(totalTime),
        imageUrl = imageUrl ?: "",
        tags = tagNames,
        steps = steps.map { it.toDomain() },
        apps = apps.map { MyActRecordDetailApp(it.packageName, it.name) },
        completedStepCount = completedStepCount,
        totalStepCount = totalStepCount
    )
