package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MyActFavoriteTagResponse(
    val tagId: String,
    val tagName: String
)
