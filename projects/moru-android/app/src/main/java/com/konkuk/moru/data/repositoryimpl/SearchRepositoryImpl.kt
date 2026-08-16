package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.response.Search.FavoriteTagAddRequest
import com.konkuk.moru.data.dto.response.Search.RoutineSearchRequest
import com.konkuk.moru.data.mapper.*
import com.konkuk.moru.data.model.FavoriteTag
import com.konkuk.moru.data.model.Page
import com.konkuk.moru.data.model.RoutineSummary
import com.konkuk.moru.data.model.SearchHistory
import com.konkuk.moru.data.model.TagItem
import com.konkuk.moru.data.service.SearchService

import com.konkuk.moru.domain.repository.SearchRepository
import com.konkuk.moru.domain.repository.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchService
) : SearchRepository {

    override suspend fun getRoutineNameHistories(): List<SearchHistory> =
        withContext(Dispatchers.IO) {
            api.getRoutineNameHistories().map { it.toDomain() }
        }

    override suspend fun deleteHistory(historyId: String) = withContext(Dispatchers.IO) {
        api.deleteSearchHistory(historyId)
        Unit
    }

    override suspend fun deleteAllHistories() = withContext(Dispatchers.IO) {
        api.deleteAllSearchHistory()
        Unit
    }

    override suspend fun getTitleSuggestions(keyword: String): List<String> =
        withContext(Dispatchers.IO) {
            if (keyword.isBlank()) emptyList() else api.getTitleSuggestions(keyword)
        }

    override suspend fun searchRoutines(
        titleKeyword: String?,
        tagNames: List<String>?,
        sortType: SortType,
        page: Int,
        size: Int
    ): Page<RoutineSummary> = withContext(Dispatchers.IO) {
        val body = RoutineSearchRequest(
            titleKeyword = titleKeyword?.ifBlank { null },
            tagNames = tagNames?.ifEmpty { null },
            sortType = sortType.name, // "LATEST" | "POPULAR"
            page = page,
            size = size
        )
        api.searchRoutines(body).toDomain { it.toDomain() }
    }

    override suspend fun getAllTags(): List<TagItem> {
        return api.getAllTags().map { it.toDomain() }
    }


    override suspend fun getFavoriteTags(): List<FavoriteTag> = withContext(Dispatchers.IO) {
        api.getFavoriteTags().map { it.toDomain() }
    }

    override suspend fun addFavoriteTags(tagIds: List<String>) = withContext(Dispatchers.IO) {
        api.addFavoriteTags(FavoriteTagAddRequest(tagIds))
        Unit
    }

    override suspend fun deleteFavoriteTag(tagId: String) = withContext(Dispatchers.IO) {
        api.deleteFavoriteTag(tagId)
        Unit
    }
}