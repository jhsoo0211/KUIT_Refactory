package com.konkuk.moru.domain.model

data class MyActRecordCursor(
    val createdAt: String,
    val logId: String
)

data class MyActRecordPage(
    val items: List<MyActRecord>,
    val hasNext: Boolean,
    val nextCursor: MyActRecordCursor?
)
