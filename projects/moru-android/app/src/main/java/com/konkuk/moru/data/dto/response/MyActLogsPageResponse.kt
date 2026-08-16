package com.konkuk.moru.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MyActLogsPageResponse(
    @SerialName("content") val content: List<MyActLogItemDto>,
    @SerialName("hasNext") val hasNext: Boolean,
    @SerialName("nextCursor") val nextCursor: MyActLogsCursorDto? = null
)

@Serializable
data class MyActLogItemDto(
    @SerialName("logId") val logId: String,
    @SerialName("routineTitle") val routineTitle: String,
    @SerialName("isCompleted") val isCompleted: Boolean,
    @SerialName("totalTime") val totalTime: JsonElement? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("tags") val tags: List<String> = emptyList()
)

@Serializable
data class MyActLogsCursorDto(
    @SerialName("createdAt") val createdAt: String,
    @SerialName("logId") val logId: String
)
