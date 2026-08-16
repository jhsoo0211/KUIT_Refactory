package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineDetailDto
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineUi
import com.konkuk.moru.data.dto.response.MyRoutine.TagDto
import java.time.DayOfWeek

interface MyRoutineRepository {
    suspend fun getMyRoutines(
        sortType: String,        // "TIME" | "LATEST" | "POPULAR"
        dayOfWeek: DayOfWeek?,   // TIME일 때만 사용
        page: Int,
        size: Int
    ): List<MyRoutineUi>


    suspend fun createSchedule(
        routineId: String,
        time: String,                  // "HH:mm:ss"
        days: Set<DayOfWeek>,
        alarm: Boolean
    ): Boolean

    suspend fun getRoutineDetail(routineId: String): MyRoutineUi

    // [추가] 상세 원본 DTO (디테일 화면 매핑용)
    suspend fun getRoutineDetailRaw(routineId: String): MyRoutineDetailDto

    suspend fun getSchedules(routineId: String): List<MyRoutineSchedule>


    // ✅ 안전 삭제 (스케줄 먼저 지우고 재시도)
    suspend fun deleteRoutineSafe(routineId: String): Boolean
    suspend fun deleteRoutine(routineId: String)

    suspend fun deleteAllSchedules(routineId: String)
    suspend fun deleteSchedule(routineId: String, scheduleId: String)

    // ✅ PATCH: 스케줄 수정
    suspend fun updateSchedule(
        routineId: String,
        schId: String,
        time: String,                 // "HH:mm:ss"
        days: Set<DayOfWeek>,
        alarm: Boolean
    ): List<MyRoutineSchedule>

    // ===== [추가] 루틴 PATCH =====
    suspend fun patchRoutine(
        routineId: String,
        title: String? = null,
        imageUrl: String? = null,
        tagNames: List<String>? = null,
        description: String? = null,
        steps: List<Triple<String, Int, String?>>? = null, // (name, order, iso8601)
        selectedApps: List<String>? = null,
        isSimple: Boolean? = null,
        isUserVisible: Boolean? = null
    )

    // ===== [추가] 스텝 =====
    suspend fun getSteps(routineId: String): List<MyRoutineDetailDto.StepDto>
    suspend fun addSteps(routineId: String, steps: List<Triple<String, Int, String?>>)
    suspend fun patchSteps(routineId: String, stepId: String, steps: List<Triple<String, Int, String?>>)
    suspend fun deleteStep(routineId: String, stepId: String)

    // ===== [추가] 태그 =====
    suspend fun getRoutineTags(routineId: String): List<TagDto>
    suspend fun addRoutineTags(routineId: String, tagIds: List<String>): List<TagDto>
    suspend fun removeRoutineTag(routineId: String, tagId: String)

    // ===== [추가] 이미지 업로드 =====
    suspend fun uploadImageAndGetUrl(fileName: String, bytes: ByteArray, mime: String): String


    private fun inferRepeatType(days: Set<java.time.DayOfWeek>): String = when {
        days.size == 7 -> "EVERYDAY"
        days == setOf(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        ) -> "WEEKDAYS"
        days == setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY) -> "WEEKENDS"
        else -> "CUSTOM"
    }



}

// [추가] 스케줄 도메인도 접두어
data class MyRoutineSchedule(
    val id: String,
    val dayOfWeek: String, // "MON"...
    val time: String,      // "HH:mm:ss"
    val alarmEnabled: Boolean,
    val repeatType: String? = null, // ⬅︎ 추가
    val daysToCreate: List<String>? = null // ⬅︎ 추가
)