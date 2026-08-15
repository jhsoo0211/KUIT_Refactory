package com.konkuk.moru.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchMyRoutineRequest(
    val title: String? = null,
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),        // 서버가 이름 배열 허용
    val description: String? = null,
    val steps: List<List<StepPatch>> = emptyList(), // 스웨거가 2중 배열 예시라 안전하게 래핑
    val selectedApps: List<String> = emptyList(),
    val isSimple: Boolean? = null,
    val isUserVisible: Boolean? = null
) {
    @Serializable
    data class StepPatch(
        val name: String,
        val stepOrder: Int,
        val estimatedTime: String? = null // "PT5M" 형식
    )
}


@Serializable
data class AddTagsRequest(
    val tagIds: List<String>
)