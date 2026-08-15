package com.konkuk.moru.data.model



data class TagItem(
    val id: String,
    val name: String,
    val createdAtIso: String // 서버가 주는 createdAt 보존 (필요 시 정렬 등에 사용)
)


data class RoutineSummary(
    val routineId: String,
    val title: String,
    val imageUrl: String?,
    val tags: List<String>,
    val likeCount: Int,
    val createdAtIso: String,
    val isRunning: Boolean,
    val isLiked: Boolean = false // 서버 미제공 -> 기본 false
)

data class SearchHistory(
    val id: String,
    val keyword: String,
    val createdAtIso: String
)

data class FavoriteTag(
    val id: String,
    val name: String
)

data class Page<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long,
    val isFirst: Boolean,
    val isLast: Boolean
)