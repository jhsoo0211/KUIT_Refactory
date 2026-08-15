package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.dto.response.UserProfile.UserProfileResponse

interface UserRepository {
    suspend fun getUserProfile(): UserProfileResponse
} 