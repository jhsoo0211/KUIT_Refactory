package com.konkuk.moru.domain.model

data class Insight(
    val paceGrade: String,
    val routineCompletionRate: Double,
    val globalAverageRoutineCompletionRate: Double,
    val completionDistribution: Map<String, Int>,
    val weekdayUser: Double,
    val weekdayOverall: Double,
    val weekendUser: Double,
    val weekendOverall: Double,
    val completionByTimeSlot: Map<String, Int>
)
