package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MyActTagResponse(
    val id: String,
    val name: String,
    val createdAt: String
)
