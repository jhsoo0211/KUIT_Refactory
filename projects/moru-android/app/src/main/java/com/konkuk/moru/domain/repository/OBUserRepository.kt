package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.dto.request.FavoriteTagRequest
import com.konkuk.moru.data.dto.response.ServerTag
import com.konkuk.moru.data.dto.response.UpdateUserProfileResponse
import java.io.File

interface OBUserRepository {
    suspend fun checkNicknameAvailable(nickname: String): Result<Boolean>

    //    suspend fun updateProfile(body: UpdateUserProfileRequest): Result<UpdateUserProfileResponse>
    suspend fun updateProfileDynamic(
        body: Map<String, String>
    ): Result<UpdateUserProfileResponse>

    suspend fun getAllTags(): Result<List<ServerTag>>
    suspend fun addFavoriteTags(body: FavoriteTagRequest): Result<Unit>
    suspend fun uploadImage(file: File): Result<String>
}