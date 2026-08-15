package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.CreateRoutineResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateRoutineService {
    @POST("api/routines")
    suspend fun createRoutine(@Body body: Any): Response<CreateRoutineResponse>
}