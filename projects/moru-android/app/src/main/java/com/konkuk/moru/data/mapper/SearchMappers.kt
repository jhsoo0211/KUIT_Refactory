package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.Search.FavoriteTagItemResponse
import com.konkuk.moru.data.dto.response.Search.PageResponse
import com.konkuk.moru.data.dto.response.Search.RoutineSummaryResponse
import com.konkuk.moru.data.dto.response.Search.SearchHistoryResponse
import com.konkuk.moru.data.dto.response.Search.TagAllResponse
import com.konkuk.moru.data.model.FavoriteTag
import com.konkuk.moru.data.model.Page
import com.konkuk.moru.data.model.RoutineSummary
import com.konkuk.moru.data.model.SearchHistory
import com.konkuk.moru.data.model.TagItem


fun RoutineSummaryResponse.toDomain() = RoutineSummary(
    routineId = id,
    title = title,
    imageUrl = imageUrl,
    tags = tags,
    likeCount = likeCount,
    createdAtIso = createdAt,
    isRunning = isRunning,
    isLiked = false // 변경: 서버 응답에 isLiked 없음 -> 기본 false
)

fun SearchHistoryResponse.toDomain() = SearchHistory(
    id = id,
    keyword = searchKeyword,
    createdAtIso = createdAt
)

fun FavoriteTagItemResponse.toDomain() = FavoriteTag(
    id = tagId,
    name = tagName
)

fun <T, R> PageResponse<T>.toDomain(mapper: (T) -> R) = Page(
    content = content.map(mapper),
    page = number,
    size = size,
    totalPages = totalPages,
    totalElements = totalElements,
    isFirst = first,
    isLast = last
)

// [ADD] 전체 태그 매핑
fun TagAllResponse.toDomain() = TagItem(
    id = id,
    name = name,
    createdAtIso = createdAt
)