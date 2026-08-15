package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.MyActTagResponse
import com.konkuk.moru.domain.model.MyActTag

fun MyActTagResponse.toDomain(): MyActTag =
    MyActTag(
        id = id,
        name = name,
        createdDate = createdAt.take(10).replace("-", ".") // 2025-08-16 -> 2025.08.16
    )