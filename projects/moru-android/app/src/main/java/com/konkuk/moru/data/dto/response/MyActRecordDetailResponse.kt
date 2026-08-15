package com.konkuk.moru.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MyActRecordDetailResponse(
    @SerialName("id") val id: String,
    @SerialName("routineTitle") val routineTitle: String,
    @SerialName("isSimple") val isSimple: Boolean,
    @SerialName("isCompleted") val isCompleted: Boolean,
    @SerialName("startedAt") val startedAt: String,     // ISO-8601
    @SerialName("endedAt") val endedAt: String? = null, // ISO-8601 or null
    @SerialName("totalTime") val totalTime: JsonElement? = null, // 문자열 or 객체
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("tagNames") val tagNames: List<String> = emptyList(),
    @SerialName("steps") val steps: List<MyActRecordDetailStepDto> = emptyList(),
    @SerialName("apps") val apps: List<MyActRecordDetailAppDto> = emptyList(),
    @SerialName("completedStepCount") val completedStepCount: Int = 0,
    @SerialName("totalStepCount") val totalStepCount: Int = 0
)

@Serializable
data class MyActRecordDetailStepDto(
    @SerialName("stepOrder") val stepOrder: Int,
    @SerialName("stepName") val stepName: String,
    @SerialName("note") val note: String? = null,
    @SerialName("estimatedTime") val estimatedTime: JsonElement? = null,
    @SerialName("actualTime") val actualTime: JsonElement? = null,
    @SerialName("isCompleted") val isCompleted: Boolean
)

@Serializable
data class MyActRecordDetailAppDto(
    @SerialName("packageName") val packageName: String,
    @SerialName("name") val name: String
)
