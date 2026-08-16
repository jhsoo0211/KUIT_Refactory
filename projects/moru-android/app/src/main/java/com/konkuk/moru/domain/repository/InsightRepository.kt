package com.konkuk.moru.domain.repository

import com.konkuk.moru.domain.model.Insight

interface InsightRepository {
    suspend fun getInsights(): Result<Insight>
    suspend fun completeRoutine(routineId: String): Result<Unit>
}