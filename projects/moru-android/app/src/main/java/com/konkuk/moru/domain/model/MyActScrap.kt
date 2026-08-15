package com.konkuk.moru.domain.model

data class MyActScrap(
    val routineId: String,
    val title: String,
    val imageUrl: String,
    val tagNames: List<String>)

data class MyActScrapCursor(
    val createdAt: String,
    val scrapId: String
)

data class MyActScrapsPage(
    val items: List<MyActScrap>,
    val hasNext: Boolean,
    val nextCursor: MyActScrapCursor?
)

