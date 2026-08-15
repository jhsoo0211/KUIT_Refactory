package com.konkuk.moru.data.dto.request

import com.google.gson.annotations.SerializedName

// [신규] isSimple=false 전용 요청 DTO (estimatedTime 포함)
data class CreateRoutineFocusRequest(
    @SerializedName("title") val title: String,
    @SerializedName("imageKey") val imageKey: String?,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("description") val description: String,
    @SerializedName("steps") val steps: List<FocusStepDto>, // 시간 필드 포함 Step
    @SerializedName("selectedApps") val selectedApps: List<String>,
    @SerializedName("isSimple") val isSimple: Boolean,
    @SerializedName("isUserVisible") val isUserVisible: Boolean
)

// [신규] 집중 루틴 Step (시간 포함)
data class FocusStepDto(
    @SerializedName("name") val name: String,
    @SerializedName("stepOrder") val stepOrder: Int,
    @SerializedName("estimatedTime") val estimatedTime: String // ISO-8601 duration (e.g., PT5M)
)