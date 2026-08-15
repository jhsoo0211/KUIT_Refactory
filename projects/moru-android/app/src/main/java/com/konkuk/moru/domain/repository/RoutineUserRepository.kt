package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.model.RoutineUserProfileDomain


interface RoutineUserRepository {
    suspend fun getMe(): RoutineUserProfileDomain
    suspend fun getUserProfile(userId: String): RoutineUserProfileDomain
}

