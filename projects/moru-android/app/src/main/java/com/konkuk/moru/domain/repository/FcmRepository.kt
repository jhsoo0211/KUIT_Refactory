package com.konkuk.moru.domain.repository

interface FcmRepository {
    suspend fun registerFcmToken(token: String): Result<Unit>
}