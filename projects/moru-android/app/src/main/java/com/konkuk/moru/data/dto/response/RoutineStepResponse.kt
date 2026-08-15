package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class RoutineStepResponse(
    val id: String,
    @SerialName("stepOrder")
    val order: Int,
    val name: String,
    @SerialName("estimatedTime")
    val duration: String? = null,
    val description: String? = null
)
