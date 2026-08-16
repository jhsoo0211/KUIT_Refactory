package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.request.RoutineFeedCreateRequest
import com.konkuk.moru.data.dto.response.RoutinePageResponse
import com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponseV1
import com.konkuk.moru.data.dto.response.Routine.RoutineFeedCreateResponse
import com.konkuk.moru.data.dto.response.RoutineStepResponse
import com.konkuk.moru.data.dto.response.HomeScheduleResponse
import com.konkuk.moru.data.mapper.toBackend
import com.konkuk.moru.data.service.RoutineService
import java.time.LocalDate
import javax.inject.Inject

class RoutineRepository @Inject constructor(
    private val service: RoutineService
) {
    suspend fun getMyRoutinesToday(page: Int = 0, size: Int = 20): RoutinePageResponse {
        val day = LocalDate.now().dayOfWeek.toBackend()
        return service.getMyRoutinesToday(
            sortType = "TIME",
            dayOfWeek = day,
            page = page,
            size = size
        )
    }

    suspend fun getAllMyRoutines(page: Int = 0, size: Int = 100): RoutinePageResponse {
        return service.getAllMyRoutines(sortType = "LATEST", page = page, size = size)
    }

    suspend fun getRoutineDetail(routineId: String): RoutineDetailResponseV1 {
        return service.getRoutineDetail(routineId)
    }

    suspend fun getRoutineSteps(routineId: String): List<RoutineStepResponse> {
        return service.getRoutineSteps(routineId)
    }

    // =================== [변경] RoutineFeed 접두 DTO 사용 ===================
    suspend fun createRoutine(body: RoutineFeedCreateRequest): RoutineFeedCreateResponse =
        service.createRoutine(body)


    suspend fun getRoutineSchedules(routineId: String): List<HomeScheduleResponse> {
        return try {
            val schedules = service.getRoutineSchedules(routineId)
            schedules
        } catch (e: Exception) {
            throw e
        }
    }
}
