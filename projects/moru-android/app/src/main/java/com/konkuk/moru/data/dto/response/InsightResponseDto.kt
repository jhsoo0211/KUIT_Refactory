package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class InsightResponseDto(
    val paceGrade: String,
    val routineCompletionRate: Double,
    val globalAverageRoutineCompletionRate: Double,
    val completionDistribution: Map<String, Int> = emptyMap(), // 동적 키
    val averageRoutineCount: AverageRoutineCountDto,
    val routineCompletionCountByTimeSlot: Map<String, Int> = emptyMap() // 동적 키, 빈 객체 가능
)

@Serializable
data class AverageRoutineCountDto(
    val weekday: UserOverallCountDto,
    val weekend: UserOverallCountDto
)

@Serializable
data class UserOverallCountDto(
    val user: Double,
    val overall: Double
)

