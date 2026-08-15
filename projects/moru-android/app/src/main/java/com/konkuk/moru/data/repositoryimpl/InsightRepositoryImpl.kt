package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.mapper.toDomain
import com.konkuk.moru.domain.model.Insight
import com.konkuk.moru.data.service.InsightService
import com.konkuk.moru.domain.repository.InsightRepository
import javax.inject.Inject

class InsightRepositoryImpl @Inject constructor(
    private val service: InsightService
) : InsightRepository {
    override suspend fun getInsights(): Result<Insight> = runCatching {
        service.getInsights().toDomain()
    }
    
    override suspend fun completeRoutine(routineId: String): Result<Unit> = runCatching {
        service.completeRoutine(routineId)
    }
}