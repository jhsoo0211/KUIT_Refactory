package com.konkuk.moru.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleUpsertRequest(
    val repeatType: String,            // ← 추가
    val daysToCreate: List<String>,    // ← 이름/포맷 변경
    val time: String,                  // "HH:mm:ss"
    val alarmEnabled: Boolean
)