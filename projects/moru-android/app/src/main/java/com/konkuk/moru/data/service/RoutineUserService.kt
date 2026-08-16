package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.UserProfile.RoutineUserProfileResponse
import com.konkuk.moru.presentation.routinefeed.data.UserMeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RoutineUserService {
    @GET("api/user/me")
    suspend fun getMe(): UserMeResponse

    @GET("api/user/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: String
    ): RoutineUserProfileResponse

}