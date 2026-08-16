package com.konkuk.moru.data.mapper

// [신규] 피드 상세 DTO → 피드 접두의 생성 Request 로 변환
import com.konkuk.moru.core.util.modifier.toIsoDurationOrZero
import com.konkuk.moru.data.dto.request.RoutineFeedCreateRequest
import com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponseV1
import com.konkuk.moru.data.dto.response.RoutineStepResponse

// [변경] 응답 DTO의 로컬 필드명(order, duration)에 맞춰 맵핑
fun RoutineDetailResponseV1.toFeedCreateRequest(
    steps: List<RoutineStepResponse>,
    isSimple: Boolean,                          // [추가] 외부에서 전달
    selectedApps: List<String> = emptyList()    // [추가] 외부에서 전달(없으면 빈 리스트)
): RoutineFeedCreateRequest {
    return RoutineFeedCreateRequest(
        title = this.title,
        imageKey = null, // [설명] 피드 imageUrl은 key 없음 → null
        tags = (this.tags ?: emptyList()).map { it.removePrefix("#") },
        description = this.description,
        steps = steps.map { s ->
            RoutineFeedCreateRequest.Step(
                name = s.name,
                stepOrder = s.order,                               // [유지] order → stepOrder
                estimatedTime = s.duration.toIsoDurationOrZero()   // [유지] duration → ISO-8601 보정
            )
        },
        selectedApps = selectedApps,            // [변경] this.selectedApps 참조 제거
        isSimple = isSimple,                    // [변경] this.isSimple 참조 제거
        isUserVisible = true
    )
}