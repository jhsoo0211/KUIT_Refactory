package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.request.MyActFavoriteTagSetRequestDto
import com.konkuk.moru.data.service.MyActTagService
import com.konkuk.moru.domain.model.MyActTag
import com.konkuk.moru.domain.repository.MyActTagRepository
import javax.inject.Inject
import com.konkuk.moru.data.mapper.toDomain
import retrofit2.HttpException

class MyActTagRepositoryImpl @Inject constructor(
    private val service: MyActTagService
) : MyActTagRepository {

    override suspend fun getAllTags(): List<MyActTag> =
        service.getAllTags().map { it.toDomain() }

    override suspend fun getMyFavoriteTags(): List<MyActTag> =
        service.getFavoriteTags().map { it.toDomain() }

    override suspend fun setMyFavoriteTags(tagIds: List<String>) {
        val res = service.setFavoriteTags(MyActFavoriteTagSetRequestDto(tagIds))
        if (!res.isSuccessful) throw HttpException(res)
    }

    override suspend fun deleteMyFavoriteTag(tagId: String) {
        val res = service.deleteFavoriteTag(tagId)
        if (!res.isSuccessful) throw HttpException(res)
    }
}