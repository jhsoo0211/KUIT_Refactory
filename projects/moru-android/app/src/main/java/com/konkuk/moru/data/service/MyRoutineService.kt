package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.request.ScheduleUpsertRequest
import com.konkuk.moru.data.dto.response.MyRoutine.AddTagsRequest
import com.konkuk.moru.data.dto.response.MyRoutine.MyPageResponse
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineDetailDto
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineScheduleDto
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineSummaryDto
import com.konkuk.moru.data.dto.response.MyRoutine.PatchOrCreateStepRequest
import com.konkuk.moru.data.dto.response.MyRoutine.PatchRoutineRequest
import com.konkuk.moru.data.dto.response.MyRoutine.UpdateScheduleRequest
import com.konkuk.moru.data.dto.response.MyRoutine.TagDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MyRoutineService {

    // [추가] 내 루틴 목록
    @GET("api/routines")
    suspend fun getMyRoutines(
        @Query("sortType") sortType: String = "TIME",  // TIME|LATEST|POPULAR
        @Query("dayOfWeek") dayOfWeek: String? = null, // TIME일 때 필요
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): MyPageResponse<MyRoutineSummaryDto>

    // [추가] 상세
    @GET("api/routines/{routineId}")
    suspend fun getRoutineDetail(@Path("routineId") routineId: String): MyRoutineDetailDto

    // [추가] 삭제
    @DELETE("api/routines/{routineId}")
    suspend fun deleteRoutine(@Path("routineId") routineId: String): Response<Unit>

    // [추가] 스케줄
    @GET("api/routines/{routineId}/schedules")
    suspend fun getSchedules(@Path("routineId") routineId: String): List<MyRoutineScheduleDto>

    @DELETE("api/routines/{routineId}/schedules")
    suspend fun deleteAllSchedules(@Path("routineId") routineId: String): Response<Unit>

    @DELETE("api/routines/{routineId}/schedules/{schId}")
    suspend fun deleteSchedule(
        @Path("routineId") routineId: String,
        @Path("schId") schId: String
    ): Response<Unit>

    // [추가] 스케줄 생성 (단건)
    @POST("api/routines/{routineId}/schedules")
    suspend fun createSchedule(
        @Path("routineId") routineId: String,
        @Body body: ScheduleUpsertRequest
    ): Response<List<MyRoutineScheduleDto>>


    @PATCH("api/routines/{routineId}/schedules/{schId}")
    suspend fun patchSchedule(
        @Path("routineId") routineId: String,
        @Path("schId") schId: String,
        @Body body: UpdateScheduleRequest
    ): Response<List<MyRoutineScheduleDto>>


    // ====== [추가] 루틴 수정 (PATCH) ======
    @PATCH("api/routines/{routineId}")
    suspend fun patchRoutine(
        @Path("routineId") routineId: String,
        @Body body: PatchRoutineRequest
    ): Response<Unit>

    // ====== [추가] 스텝 CRUD ======
    @GET("api/routines/{routineId}/steps")
    suspend fun getSteps(@Path("routineId") routineId: String): List<MyRoutineDetailDto.StepDto>

    @POST("api/routines/{routineId}/steps")
    suspend fun addSteps(
        @Path("routineId") routineId: String,
        @Body steps: List<PatchOrCreateStepRequest>
    ): Response<Unit>

    // 서버 스펙이 배열을 받는 PATCH (특정 stepId 경로지만 바디는 배열)
    @PATCH("api/routines/{routineId}/steps/{stepId}")
    suspend fun patchSteps(
        @Path("routineId") routineId: String,
        @Path("stepId") stepId: String,
        @Body steps: List<PatchOrCreateStepRequest>
    ): Response<Unit>

    @DELETE("api/routines/{routineId}/steps/{stepId}")
    suspend fun deleteStep(
        @Path("routineId") routineId: String,
        @Path("stepId") stepId: String
    ): Response<Unit>

    // ====== [추가] 태그 연결/해제/조회 ======
    @GET("api/routines/{routineId}/tags")
    suspend fun getRoutineTags(@Path("routineId") routineId: String): List<TagDto>

    @POST("api/routines/{routineId}/tags")
    suspend fun addRoutineTags(
        @Path("routineId") routineId: String,
        @Body body: AddTagsRequest
    ): List<TagDto>

    @DELETE("api/routines/{routineId}/tags/{tagId}")
    suspend fun removeRoutineTag(
        @Path("routineId") routineId: String,
        @Path("tagId") tagId: String
    ): Response<Unit>

}