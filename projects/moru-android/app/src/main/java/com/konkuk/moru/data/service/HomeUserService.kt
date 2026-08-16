package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.UserProfile.UserProfileResponse
import retrofit2.http.GET

interface HomeUserService {
    @GET("/api/user/me")
    suspend fun getMe(): UserProfileResponse
}
