package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeScheduleResponse(
    val id: String,
    val dayOfWeek: String,  // "MON", "TUE", "WED", etc.
    val time: String,       // "14:30:00" 형식
    val alarmEnabled: Boolean,
    val repeatType: String? = null,  // null 가능하도록 수정
    val daysToCreate: List<String>? = null  // null 가능하도록 수정
)
