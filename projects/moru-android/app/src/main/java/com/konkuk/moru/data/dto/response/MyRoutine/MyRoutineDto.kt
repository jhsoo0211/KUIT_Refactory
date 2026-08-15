package com.konkuk.moru.data.dto.response.MyRoutine

import java.time.DayOfWeek
import java.time.LocalTime

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

// [추가] 내 루틴 리스트/상세 화면 전용 UI 모델
data class MyRoutineUi(
    val routineId: String,
    val title: String,
    val imageUrl: String?,
    val tags: List<String>,
    val likes: Int,
    val isLiked: Boolean,
    val isRunning: Boolean,
    val scheduledTime: LocalTime?,      // 스케줄 조회/편집 시 사용
    val scheduledDays: Set<DayOfWeek>,  // 스케줄 조회/편집 시 사용
    val isAlarmEnabled: Boolean,        // 스케줄 조회/편집 시 사용
    val isChecked: Boolean,             // 삭제 모드용 체크박스 UI 상태
    val authorId: String?,               // 상세에서 주입
    val createdAt: String? = null // ISO-8601라면 문자열 정렬도 안전
)


@Serializable
data class MyPageResponse<T>(
    val totalElements: Long? = null,
    val totalPages: Int? = null,
    val pageable: Pageable? = null,
    val size: Int? = null,
    val content: List<T> = emptyList(),
    val number: Int? = null,
    // ▼ [변경] 배열·객체 무엇이 와도 파싱되도록
    val sort: JsonElement? = null,
    val numberOfElements: Int? = null,
    val first: Boolean? = null,
    val last: Boolean? = null,
    val empty: Boolean? = null
) {
    @Serializable
    data class Pageable(
        val paged: Boolean? = null,
        val pageNumber: Int? = null,
        val pageSize: Int? = null,
        val offset: Long? = null,
        // ▼ [추가/변경] 일부 서버는 pageable 내부에도 sort 객체를 넣음
        val sort: JsonElement? = null
    )

    // [삭제] 아래 Sort 데이터클래스는 더 이상 필요 없다면 제거해도 됩니다.
    // @Serializable data class Sort(...)
//  ↑↑↑↑↑↑↑↑↑↑ 여기까지가 [변경] 포인트
}

// [추가] 목록 DTO
@Serializable
data class MyRoutineSummaryDto(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    val likeCount: Int,
    val createdAt: String? = null,
    val requiredTime: String? = null, // e.g., "PT50M"
    val isRunning: Boolean
)

// [추가] 상세 DTO
@Serializable
data class MyRoutineDetailDto(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val author: AuthorDto,
    val tags: List<String> = emptyList(),
    val description: String? = null,
    val isSimple: Boolean,
    val isUserVisible: Boolean,
    val steps: List<StepDto> = emptyList(),
    val apps: List<AppDto> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val requiredTime: String? = null,
    val likeCount: Int,
    val scrapCount: Int,
    val isLiked: Boolean,
    val isScrapped: Boolean,
    val isOwner: Boolean,
    val similarRoutines: List<SimilarDto> = emptyList()
) {
    @Serializable
    data class AuthorDto(val id: String, val nickname: String, val profileImageUrl: String? = null)
    @Serializable
    data class StepDto(
        val id: String,
        val stepOrder: Int,
        val name: String,
        val estimatedTime: String? = null
    )

    @Serializable
    data class AppDto(val packageName: String, val name: String)
    @Serializable
    data class SimilarDto(
        val id: String,
        val title: String,
        val imageUrl: String? = null,
        val tag: String? = null
    )
}

// [추가] 스케줄 DTO
@Serializable
data class MyRoutineScheduleDto(
    val id: String,
    val dayOfWeek: String, // "MON"~"SUN"
    val time: String,      // "HH:mm:ss"
    val alarmEnabled: Boolean,
    val repeatType: String? = null,
    val daysToCreate: List<String>? = null
)

@Serializable
data class UpdateScheduleRequest(
    val repeatType: String,
    val daysToCreate: List<String>,
    val time: String,            // "HH:mm:ss"
    val alarmEnabled: Boolean
)

// [추가] 서버 스펙에 맞춘 PATCH 바디
@Serializable
data class PatchRoutineRequest(
    val title: String? = null,
    val imageUrl: String? = null,
    val tags: List<String>? = null,                // 이름 배열 (스웨거 예시 기준)
    val description: String? = null,
    val steps: List<PatchOrCreateStepRequest>? = null, // [[{...}]] 형태 주의
    val selectedApps: List<String>? = null,        // 패키지명 배열
    val isSimple: Boolean? = null,
    val isUserVisible: Boolean? = null
)

// [추가] STEP 생성/수정 공용 바디
@Serializable
data class PatchOrCreateStepRequest(
    val name: String,
    val stepOrder: Int,
    val estimatedTime: String? // ISO-8601 ex) "PT5M"
)

// [추가] 태그 연결 요청 (최대 3개)
@Serializable
data class AddTagsRequest(
    val tagIds: List<String>
)