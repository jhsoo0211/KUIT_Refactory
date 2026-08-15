package com.konkuk.moru.data.service


import com.konkuk.moru.data.dto.request.FavoriteTagRequest
import com.konkuk.moru.data.dto.response.NicknameCheckResponse
import com.konkuk.moru.data.dto.response.ServerTag
import com.konkuk.moru.data.dto.response.UpdateUserProfileResponse
import com.konkuk.moru.data.dto.response.OBImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface OBUserService {
    @GET("/api/user/nickname/{nickname}")
    suspend fun checkNickname(
        @Path(value = "nickname", encoded = true) nickname: String
    ): Response<NicknameCheckResponse>

    @PATCH("/api/user/me")
    suspend fun updateMeDynamic(
        @Body body: Map<String, String>
    ): Response<UpdateUserProfileResponse>

    @GET("/api/tags")
    suspend fun getAllTags(): Response<List<ServerTag>>

    @POST("/api/user/favorite-tag")
    suspend fun addFavoriteTags(
        @Body body: FavoriteTagRequest
    ): Response<Unit>

    // 업로드(인증 필요)
    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<OBImageUploadResponse>
}