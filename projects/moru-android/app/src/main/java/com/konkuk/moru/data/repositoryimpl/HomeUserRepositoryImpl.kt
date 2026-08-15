package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.response.UserProfile.UserProfileResponse
import com.konkuk.moru.data.service.HomeUserService
import com.konkuk.moru.domain.repository.UserRepository
import javax.inject.Inject

class HomeUserRepositoryImpl @Inject constructor(
    private val service: HomeUserService
) : UserRepository {

    override suspend fun getUserProfile(): UserProfileResponse {
        return service.getMe()
    }
}
