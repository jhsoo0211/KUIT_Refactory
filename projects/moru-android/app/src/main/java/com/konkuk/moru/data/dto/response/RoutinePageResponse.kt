package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class RoutinePageResponse(
    val content: List<RoutineResponse> = emptyList(),
    val pageable: PageableInfo = PageableInfo(),
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val last: Boolean = false,
    val size: Int = 0,
    val number: Int = 0,
    val sort: SortInfo = SortInfo(),
    val numberOfElements: Int = 0,
    val first: Boolean = false,
    val empty: Boolean = false
)

@Serializable
data class PageableInfo(
    val pageNumber: Int = 0,
    val pageSize: Int = 0,
    val sort: SortInfo = SortInfo(),
    val offset: Int = 0,
    val paged: Boolean = false,
    val unpaged: Boolean = false
)

@Serializable
data class SortInfo(
    val sorted: Boolean = false,
    val empty: Boolean = false,
    val unsorted: Boolean = false
)
