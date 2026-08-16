package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.response.CreateRoutineResponse
import com.konkuk.moru.data.service.CreateRoutineService
import com.konkuk.moru.domain.repository.CreateRoutineRepository
import retrofit2.HttpException
import javax.inject.Inject

class CreateRoutineRepositoryImpl @Inject constructor(
    private val service: CreateRoutineService
) : CreateRoutineRepository {
    override suspend fun createRoutine(body: Any): Result<CreateRoutineResponse> =
        runCatching {
            val response = service.createRoutine(body)
            if (!response.isSuccessful) throw HttpException(response)
            response.body() ?: throw IllegalStateException("Empty body")
        }
}
