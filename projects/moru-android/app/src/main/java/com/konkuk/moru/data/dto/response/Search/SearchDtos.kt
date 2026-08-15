package com.konkuk.moru.data.dto.response.Search


data class TagAllResponse(
    val id: String,
    val name: String,
    val createdAt: String // ISO-8601
)


data class RoutineSearchRequest(
    val titleKeyword: String?,
    val tagNames: List<String>?,
    val sortType: String, // "LATEST" | "POPULAR"
    val page: Int,
    val size: Int
)

data class PageResponse<T>(
    val totalElements: Long,
    val totalPages: Int,
    val pageable: Any?, // 서버 스펙상 필요 시 사용
    val size: Int,
    val content: List<T>,
    val number: Int,
    val sort: Any?,
    val numberOfElements: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)

data class RoutineSummaryResponse(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val tags: List<String>,
    val likeCount: Int,
    val createdAt: String, // ISO-8601
    val isRunning: Boolean
    // 서버 응답에 isLiked가 없음 -> 도메인에서 false 기본값 처리
)

data class SearchHistoryResponse(
    val id: String,
    val searchKeyword: String,
    val searchType: String,  // "ROUTINE_NAME"
    val createdAt: String    // ISO-8601
)

data class FavoriteTagItemResponse(
    val tagId: String,
    val tagName: String
)

data class FavoriteTagAddRequest(
    val tagIds: List<String>
)