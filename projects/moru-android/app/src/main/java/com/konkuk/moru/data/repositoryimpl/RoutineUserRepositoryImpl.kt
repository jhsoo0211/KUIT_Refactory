package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.mapper.toDomain
import com.konkuk.moru.data.model.RoutineUserProfileDomain
import com.konkuk.moru.data.service.RoutineUserService
import com.konkuk.moru.domain.repository.RoutineUserRepository
import javax.inject.Inject

class RoutineUserRepositoryImpl @Inject constructor(
    private val routineuserService: RoutineUserService
) : RoutineUserRepository {
    override suspend fun getMe(): RoutineUserProfileDomain = routineuserService.getMe().toDomain()

    override suspend fun getUserProfile(userId: String): RoutineUserProfileDomain =
        routineuserService.getUserProfile(userId).toDomain(userId)
}


