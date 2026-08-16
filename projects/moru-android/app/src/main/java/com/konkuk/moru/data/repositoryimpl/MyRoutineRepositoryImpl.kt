package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.request.ScheduleUpsertRequest
import com.konkuk.moru.data.dto.response.MyRoutine.AddTagsRequest
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineDetailDto
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineSummaryDto
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineUi
import com.konkuk.moru.data.dto.response.MyRoutine.PatchOrCreateStepRequest
import com.konkuk.moru.data.dto.response.MyRoutine.PatchRoutineRequest
import com.konkuk.moru.data.dto.response.MyRoutine.UpdateScheduleRequest
import com.konkuk.moru.data.mapper.toMyApiString
import com.konkuk.moru.data.mapper.toMyUi
import com.konkuk.moru.data.service.ImageService
import com.konkuk.moru.data.service.MyRoutineService
import com.konkuk.moru.domain.repository.MyRoutineRepository
import com.konkuk.moru.domain.repository.MyRoutineSchedule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import javax.inject.Inject

class MyRoutineRepositoryImpl @Inject constructor(
    private val service: MyRoutineService,
    private val imageService: ImageService // [추가]
) : MyRoutineRepository {


    private fun inferRepeatType(days: Set<java.time.DayOfWeek>): String = when {
        days.size == 7 -> "EVERYDAY"
        days == setOf(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        ) -> "WEEKDAYS"

        days == setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY) -> "WEEKENDS"
        else -> "CUSTOM"
    }

    override suspend fun getMyRoutines(
        sortType: String,
        dayOfWeek: DayOfWeek?,
        page: Int,
        size: Int
    ): List<MyRoutineUi> {
        val dayParam = dayOfWeek?.toMyApiString()

        // ✅ 폴백: TIME + day=null -> 전체 목록을 불러와 클라에서 시간순 정렬
        if (sortType == "TIME" && dayParam == null) {
            val all = service.getMyRoutines(
                sortType = "LATEST", // 전체를 가장 쉽게 가져오는 소트로 호출
                dayOfWeek = null,
                page = page,
                size = size
            ).content

            val sorted = all.sortedWith(
                compareBy<MyRoutineSummaryDto> {
                    it.requiredTime.parseIsoDurationSecondsOrNull() ?: Long.MAX_VALUE // null은 맨 뒤
                }.thenBy { it.title } // 동점 안정 정렬
            )
            return sorted.map { it.toMyUi() }
        }

        // 기존 흐름
        val res = service.getMyRoutines(
            sortType = sortType,
            dayOfWeek = dayParam,
            page = page,
            size = size
        )
        val dtoList = res.content
        val sortedDto = when (sortType) {
            "POPULAR" -> dtoList.sortedByDescending { it.likeCount }
            "LATEST" -> dtoList.sortedByDescending { toEpochFlexible(it.createdAt) }
            else -> dtoList // TIME(+day 지정)은 서버 정렬 신뢰
        }
        return sortedDto.map { it.toMyUi() }
    }

    private fun String?.parseIsoDurationSecondsOrNull(): Long? = try {
        this?.let { java.time.Duration.parse(it).seconds }
    } catch (_: Exception) {
        null
    }
    /*override suspend fun getMyRoutines(
        sortType: String,
        dayOfWeek: DayOfWeek?,
        page: Int,
        size: Int
    ): List<MyRoutineUi> {
        // [변경] TIME인데 dayOfWeek가 없으면 서버가 기대대로 정렬 안 줄 수 있어 null 그대로 전달
        //       (TIME의 기본 요일 처리는 ViewModel에서 '오늘 요일'로 채워 줍니다.)
        val dayParam = dayOfWeek?.toMyApiString()

        val res = service.getMyRoutines(
            sortType = sortType,
            dayOfWeek = dayParam,
            page = page,
            size = size
        )

        // [추가] 서버가 정렬을 무시하거나 일관적이지 않을 때를 대비해
        //        POPULAR / LATEST는 클라이언트에서 한 번 더 보정 정렬
        val dtoList = res.content

        val sortedDto: List<MyRoutineSummaryDto> = when (sortType) {
            "POPULAR" -> dtoList.sortedByDescending { it.likeCount } // [추가] 좋아요 내림차순
            "LATEST" -> dtoList.sortedByDescending { toEpochFlexible(it.createdAt) }// [추가] 최신순
            else -> dtoList // TIME은 서버 기준(요일 필요) 유지
        }

        // [유지] 정렬된 DTO → UI 매핑 (likeCount → likes 매핑 포함)
        return sortedDto.map { it.toMyUi() }
    }*/

    private fun toEpoch(iso: String): Long = try {
        // 예) "2025-08-15T02:46:24.026Z"
        Instant.parse(iso).toEpochMilli()
    } catch (_: DateTimeParseException) {
        0L
    }

    private fun toEpochFlexible(s: String?): Long {
        if (s.isNullOrBlank()) return 0L
        return try {
            // 1) 2025-08-15T05:45:03.725Z 형태
            Instant.parse(s).toEpochMilli()
        } catch (_: Exception) {
            try {
                // 2) 2025-08-15T16:26:06.764185 (존 없음, 마이크로초도 허용)
                val parser = DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                    .optionalEnd()
                    .toFormatter()
                LocalDateTime.parse(s, parser).atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()
            } catch (_: Exception) {
                0L
            }
        }
    }

    override suspend fun getRoutineDetail(routineId: String): MyRoutineUi {
        return service.getRoutineDetail(routineId).toMyUi()
    }

    // ===== [추가] 상세 RAW =====
    override suspend fun getRoutineDetailRaw(routineId: String): MyRoutineDetailDto {
        return service.getRoutineDetail(routineId)
    }


    /**
     * Deletes only the routine itself.
     *
     * A failed routine DELETE must not trigger schedule cleanup: an authorization or validation
     * failure does not prove that deleting related data is safe. HTTP 404 is idempotent success
     * because the routine is already absent.
     */
    override suspend fun deleteRoutineSafe(routineId: String): Boolean {
        return try {
            val response = service.deleteRoutine(routineId)
            response.isSuccessful || response.code() == 404
        } catch (_: IOException) {
            // Offline and transport failures are recoverable UI failures, not deletion success.
            false
        }
    }

    override suspend fun createSchedule(
        routineId: String,
        time: String,
        days: Set<DayOfWeek>,
        alarm: Boolean
    ): Boolean {
        require(days.isNotEmpty()) { "days is empty. At least one day must be selected." }

        val body = ScheduleUpsertRequest(
            repeatType = inferRepeatType(days),
            daysToCreate = days.map { it.toMyApiString() },
            time = time,
            alarmEnabled = alarm
        )
        val res = service.createSchedule(routineId, body)
        if (!res.isSuccessful) {
            throw IllegalStateException("createSchedule failed: HTTP ${res.code()}")
        }
        // (선택) 서버가 리스트를 돌려주면 비었을 때 실패 취급해도 됨
        val created = res.body().orEmpty()
        if (created.isEmpty()) {
            throw IllegalStateException("createSchedule returned an empty result")
        }
        return true
    }

    override suspend fun deleteRoutine(routineId: String) {
        val r = service.deleteRoutine(routineId)
        if (!r.isSuccessful) throw IllegalStateException("Failed to delete routine: ${r.code()}")
    }

    // 스케줄 관련 로직
    override suspend fun getSchedules(routineId: String): List<MyRoutineSchedule> {
        val list = service.getSchedules(routineId)
        return list.map {
            MyRoutineSchedule(
                id = it.id,
                dayOfWeek = it.dayOfWeek,          // 서버가 주는 "MON" 등 3글자 그대로
                time = it.time ?: "00:00:00",      // null 보호
                alarmEnabled = it.alarmEnabled ?: false,
                repeatType = it.repeatType,        // (있으면 그대로)
                daysToCreate = it.daysToCreate     // (있으면 그대로)
            )
        }
    }

    override suspend fun deleteAllSchedules(routineId: String) {
        val r = service.deleteAllSchedules(routineId)
        if (!r.isSuccessful) throw IllegalStateException("Failed to delete all schedules")
    }

    override suspend fun deleteSchedule(routineId: String, scheduleId: String) {
        val r = service.deleteSchedule(routineId, scheduleId)
        if (!r.isSuccessful) throw IllegalStateException("Failed to delete schedule $scheduleId")
    }

    override suspend fun updateSchedule(
        routineId: String,
        schId: String,
        time: String,
        days: Set<DayOfWeek>,
        alarm: Boolean
    ): List<MyRoutineSchedule> {
        val req = UpdateScheduleRequest(
            repeatType = "CUSTOM",
            daysToCreate = days.map { it.toMyApiString() },
            time = time,
            alarmEnabled = alarm
        )

        val response = service.patchSchedule(routineId, schId, req)
        if (!response.isSuccessful) {
            // Do not read or expose an error body: it can contain server diagnostics or credentials.
            throw IllegalStateException("updateSchedule failed: HTTP ${response.code()}")
        }

        return response.body().orEmpty().map { dto ->
            MyRoutineSchedule(
                id = dto.id,
                dayOfWeek = dto.dayOfWeek,
                time = dto.time,
                alarmEnabled = dto.alarmEnabled,
                repeatType = dto.repeatType,
                daysToCreate = dto.daysToCreate
            )
        }
    }


    // ===== [추가] 루틴 PATCH =====
    override suspend fun patchRoutine(
        routineId: String,
        title: String?,
        imageUrl: String?,
        tagNames: List<String>?,
        description: String?,
        steps: List<Triple<String, Int, String?>>?,
        selectedApps: List<String>?,
        isSimple: Boolean?,
        isUserVisible: Boolean?
    ) {
        val steps1d = steps?.map { (name, order, iso) ->
            PatchOrCreateStepRequest(
                name = name,
                stepOrder = order,
                estimatedTime = iso
            )
        }
        val body = PatchRoutineRequest(
            title = title,
            imageUrl = imageUrl,
            tags = tagNames,
            description = description,
            steps = steps1d,
            selectedApps = selectedApps,
            isSimple = isSimple,
            isUserVisible = isUserVisible
        )
        val res = service.patchRoutine(routineId, body)
        if (res.isSuccessful) return

        // ❗ 서버가 500이면 selectedApps만 제외하고 한번 더 시도
        if (res.code() == 500 && !selectedApps.isNullOrEmpty()) {
            val bodyWithoutApps = body.copy(selectedApps = null)
            val retry = service.patchRoutine(routineId, bodyWithoutApps)
            if (retry.isSuccessful) return
        }
        error("patchRoutine failed: ${res.code()}")
    }

    // ===== [추가] STEP =====
    override suspend fun getSteps(routineId: String): List<MyRoutineDetailDto.StepDto> =
        service.getSteps(routineId)

    override suspend fun addSteps(routineId: String, steps: List<Triple<String, Int, String?>>) {
        val body = steps.map { (n, o, iso) -> PatchOrCreateStepRequest(n, o, iso) }
        val res = service.addSteps(routineId, body)
        if (!res.isSuccessful) error("addSteps failed: ${res.code()}")
    }

    override suspend fun patchSteps(
        routineId: String,
        stepId: String,
        steps: List<Triple<String, Int, String?>>
    ) {
        val body = steps.map { (n, o, iso) -> PatchOrCreateStepRequest(n, o, iso) }
        val res = service.patchSteps(routineId, stepId, body)
        if (!res.isSuccessful) error("patchSteps failed: ${res.code()}")
    }

    override suspend fun deleteStep(routineId: String, stepId: String) {
        val res = service.deleteStep(routineId, stepId)
        if (!res.isSuccessful) error("deleteStep failed: ${res.code()}")
    }

    // ===== [추가] TAG =====
    override suspend fun getRoutineTags(routineId: String) = service.getRoutineTags(routineId)

    override suspend fun addRoutineTags(routineId: String, tagIds: List<String>) =
        service.addRoutineTags(routineId, AddTagsRequest(tagIds))

    override suspend fun removeRoutineTag(routineId: String, tagId: String) {
        val res = service.removeRoutineTag(routineId, tagId)
        if (!res.isSuccessful) error("removeRoutineTag failed: ${res.code()}")
    }

    // ===== [추가] 이미지 업로드 =====
    // ===== [변경] 이미지 업로드 =====
    override suspend fun uploadImageAndGetUrl(
        fileName: String,
        bytes: ByteArray,
        mime: String
    ): String {
        // [변경] toMediaTypeOrNull()로 NPE/IllegalArgumentException 방지
        val media = mime.toMediaTypeOrNull() ?: "application/octet-stream".toMediaType() // [변경]
        val body: RequestBody = bytes.toRequestBody(media) // offset/len 지정 불필요
        val part = MultipartBody.Part.createFormData("file", fileName, body)

        val map = imageService.upload(part)

        // [변경] 서버가 내려주는 "imageUrl" 키 우선 파싱
        val raw = map["imageUrl"] // [추가]
            ?: map["url"]
            ?: map["location"]
            ?: map["key"]
            ?: error("upload response missing imageUrl/url/location/key")

        // [선택] 서버가 "/temp/..." 같이 '상대경로'를 준다면 절대경로로 변환하려면 아래 사용
        // return toAbsoluteUrl(BuildConfig.BASE_URL, raw)

        return raw // [변경] 일단 서버가 준 값 그대로 반환
    }

    // [선택] 상대경로 -> 절대경로 변환 유틸 (필요시 주석 해제)
    /*
    private fun toAbsoluteUrl(base: String, pathOrUrl: String): String =
        if (pathOrUrl.startsWith("http", ignoreCase = true)) pathOrUrl
        else base.trimEnd('/') + "/" + pathOrUrl.removePrefix("/")
    */
}


