package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.dto.response.CreateRoutineResponse

interface CreateRoutineRepository {
    suspend fun createRoutine(body: Any): Result<CreateRoutineResponse>
}