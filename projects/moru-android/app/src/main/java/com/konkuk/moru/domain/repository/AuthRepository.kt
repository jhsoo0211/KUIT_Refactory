package com.konkuk.moru.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Pair<String, String>>
    suspend fun signUp(email: String, password: String): Result<Unit>
}