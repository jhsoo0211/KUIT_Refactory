package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponse
import com.konkuk.moru.presentation.routinefeed.data.LiveUserInfo
import com.konkuk.moru.presentation.routinefeed.data.RoutineFeedResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoutineFeedService {

    @GET("api/routines/live-users")
    suspend fun getLiveUsers(): List<LiveUserInfo>

    @GET("api/routines/recommend/feed")
    suspend fun getRoutineFeed(): RoutineFeedResponse

    @GET("api/routines/{routineId}")
    suspend fun getRoutineDetail(
        @Path("routineId") routineId: String
    ): RoutineDetailResponse


    // 좋아요
    @POST("api/social/{routineId}/likes")
    suspend fun addLike(@Path("routineId") routineId: String)

    @DELETE("api/social/{routineId}/likes")
    suspend fun removeLike(@Path("routineId") routineId: String)

    // ✅ 스크랩(북마크)
    @POST("api/social/{routineId}/scraps")
    suspend fun addScrap(@Path("routineId") routineId: String)

    @DELETE("api/social/{routineId}/scraps")
    suspend fun removeScrap(@Path("routineId") routineId: String)


}