package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.MyActScrapCursorResponse
import com.konkuk.moru.data.dto.response.MyActScrapItemResponse
import com.konkuk.moru.data.dto.response.MyActScrapsPageResponse
import com.konkuk.moru.domain.model.MyActScrap
import com.konkuk.moru.domain.model.MyActScrapCursor
import com.konkuk.moru.domain.model.MyActScrapsPage

fun MyActScrapItemResponse.toDomain() = MyActScrap(routineId, title, imageUrl ?: "", tagNames)
fun MyActScrapCursorResponse.toDomain() = MyActScrapCursor(createdAt, scrapId)
fun MyActScrapsPageResponse.toDomain() = MyActScrapsPage(
    items = content.map { it.toDomain() }, hasNext = hasNext,
    nextCursor = nextCursor?.toDomain()
)
