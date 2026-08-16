package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.MyActFavoriteTagResponse
import com.konkuk.moru.data.dto.response.MyActTagResponse
import retrofit2.http.GET
import com.konkuk.moru.data.dto.request.MyActFavoriteTagSetRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface MyActTagService {
    @GET("api/tags")
    suspend fun getAllTags(): List<MyActTagResponse>

    @GET("api/user/favorite-tag")
    suspend fun getFavoriteTags(): List<MyActFavoriteTagResponse>

    @POST("api/user/favorite-tag")
    suspend fun setFavoriteTags(
        @Body body: MyActFavoriteTagSetRequestDto
    ): Response<Unit>

    @DELETE("api/user/favorite-tag/{tagId}")
    suspend fun deleteFavoriteTag(
        @Path("tagId") tagId: String
    ): Response<Unit>
}