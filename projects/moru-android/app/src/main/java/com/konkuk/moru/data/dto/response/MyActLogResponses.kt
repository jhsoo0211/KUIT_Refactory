package com.konkuk.moru.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyActRoutineLogTimeDto(
    @SerialName("seconds") val seconds: Long = 0,
    @SerialName("nano") val nano: Int? = null
)

@Serializable
data class MyActRoutineLogResponse(
    @SerialName("logId") val logId: String,
    @SerialName("routineTitle") val routineTitle: String,
    @SerialName("isCompleted") val isCompleted: Boolean,
    @SerialName("totalTime") val totalTime: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("tags") val tags: List<String> = emptyList()
)
