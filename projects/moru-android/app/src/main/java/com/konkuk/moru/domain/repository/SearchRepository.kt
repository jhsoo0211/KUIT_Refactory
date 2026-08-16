package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.model.FavoriteTag
import com.konkuk.moru.data.model.Page
import com.konkuk.moru.data.model.RoutineSummary
import com.konkuk.moru.data.model.SearchHistory
import com.konkuk.moru.data.model.TagItem


interface SearchRepository {
    suspend fun getRoutineNameHistories(): List<SearchHistory>
    suspend fun deleteHistory(historyId: String)
    suspend fun deleteAllHistories()

    suspend fun getTitleSuggestions(keyword: String): List<String>

    suspend fun searchRoutines(
        titleKeyword: String?,
        tagNames: List<String>?,
        sortType: SortType,
        page: Int,
        size: Int
    ): Page<RoutineSummary>

    suspend fun getAllTags(): List<TagItem>

    suspend fun getFavoriteTags(): List<FavoriteTag>
    suspend fun addFavoriteTags(tagIds: List<String>)
    suspend fun deleteFavoriteTag(tagId: String)
}

enum class SortType { LATEST, POPULAR }