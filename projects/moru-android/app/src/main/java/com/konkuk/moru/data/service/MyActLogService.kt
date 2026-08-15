package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.MyActLogsPageResponse
import com.konkuk.moru.data.dto.response.MyActRecordDetailResponse
import com.konkuk.moru.data.dto.response.MyActRoutineLogResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyActLogService {
    @GET("api/logs/today")
    suspend fun getTodayLogs(): List<MyActRoutineLogResponse>

    @GET("api/logs/recent")
    suspend fun getRecentLogs(): List<MyActRoutineLogResponse>

    @GET("api/logs")
    suspend fun getLogs(
        @Query("createdAt") createdAt: String? = null,
        @Query("logId")     logId: String? = null,
        @Query("size")      size: Int? = null
    ): MyActLogsPageResponse

    @GET("api/logs/{routineLogId}")
    suspend fun getLogDetail(@Path("routineLogId") id: String): MyActRecordDetailResponse
}
