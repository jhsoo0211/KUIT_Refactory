package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.request.RoutineFeedCreateRequest
import com.konkuk.moru.data.dto.response.RoutinePageResponse
import com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponseV1
import com.konkuk.moru.data.dto.response.Routine.RoutineFeedCreateResponse
import com.konkuk.moru.data.dto.response.RoutineStepResponse
import retrofit2.http.Body
import com.konkuk.moru.data.dto.response.HomeScheduleResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// 내 루틴 목록 조회를 위한 service
interface RoutineService {
    @GET("/api/routines")
    suspend fun getMyRoutinesToday(
        @Query("sortType") sortType: String = "TIME", // LATEST | POPULAR | TIME
        @Query("dayOfWeek") dayOfWeek: String = "MON",        // "MON" ~ "SUN"
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): RoutinePageResponse

    @GET("/api/routines")
    suspend fun getAllMyRoutines(
        @Query("sortType") sortType: String = "TIME", // LATEST | POPULAR | TIME
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 100
    ): RoutinePageResponse

    @GET("/api/routines/{routineId}")
    suspend fun getRoutineDetail(@Path("routineId") routineId: String): RoutineDetailResponseV1

    @GET("/api/routines/{routineId}/steps")
    suspend fun getRoutineSteps(@Path("routineId") routineId: String): List<RoutineStepResponse>


    @POST("/api/routines")
    suspend fun createRoutine(@Body body: RoutineFeedCreateRequest): RoutineFeedCreateResponse

    @GET("/api/routines/{routineId}/schedules")
    suspend fun getRoutineSchedules(@Path("routineId") routineId: String): List<HomeScheduleResponse>
}
