package com.konkuk.moru.data.dto.response.MyRoutine

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDto(
    val id: String,
    val dayOfWeek: String,  // 예: "MONDAY"
    val time: String,       // 예: "07:30:00"
    val alarmEnabled: Boolean
)