package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.InsightResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InsightService {
    @GET("/api/insights/")
    suspend fun getInsights(): InsightResponseDto
    
    @POST("/api/routines/{routineId}/complete")
    suspend fun completeRoutine(@Path("routineId") routineId: String)
}