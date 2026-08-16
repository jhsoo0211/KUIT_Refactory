package com.konkuk.moru.domain.repository

import com.konkuk.moru.domain.model.MyActTag

interface MyActTagRepository {
    suspend fun getAllTags(): List<MyActTag>

    suspend fun getMyFavoriteTags(): List<MyActTag>

    suspend fun setMyFavoriteTags(tagIds: List<String>)

    suspend fun deleteMyFavoriteTag(tagId: String)
}