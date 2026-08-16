package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.MyActFavoriteTagResponse
import com.konkuk.moru.domain.model.MyActTag

fun MyActFavoriteTagResponse.toDomain(): MyActTag =
    MyActTag(id = tagId, name = tagName, createdDate = "")