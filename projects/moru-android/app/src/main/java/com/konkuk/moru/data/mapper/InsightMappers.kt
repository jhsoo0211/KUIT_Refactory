package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.*
import com.konkuk.moru.domain.model.Insight

fun InsightResponseDto.toDomain(): Insight =
    Insight(
        paceGrade = paceGrade,
        routineCompletionRate = routineCompletionRate,
        globalAverageRoutineCompletionRate = globalAverageRoutineCompletionRate,
        completionDistribution = completionDistribution, // 이미 Map
        weekdayUser = averageRoutineCount.weekday.user,
        weekdayOverall = averageRoutineCount.weekday.overall,
        weekendUser = averageRoutineCount.weekend.user,
        weekendOverall = averageRoutineCount.weekend.overall,
        completionByTimeSlot = routineCompletionCountByTimeSlot // 이미 Map
    )