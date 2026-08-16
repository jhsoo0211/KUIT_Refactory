package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MyActScrapItemResponse(
    val routineId: String, val title: String,
    val imageUrl: String? = null, val tagNames: List<String> = emptyList()
)
@Serializable
data class MyActScrapCursorResponse(
    val createdAt: String, val scrapId: String
)
@Serializable
data class MyActScrapsPageResponse(
    val content: List<MyActScrapItemResponse>,
    val hasNext: Boolean,
    val nextCursor: MyActScrapCursorResponse? = null
)
