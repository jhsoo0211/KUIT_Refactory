package com.konkuk.moru.data.dto.request

import com.google.gson.annotations.SerializedName

// [신규] isSimple=true 전용 요청 DTO (estimatedTime 없음)
data class CreateRoutineSimpleRequest(
    @SerializedName("title") val title: String,
    @SerializedName("imageKey") val imageKey: String?,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("description") val description: String,
    @SerializedName("steps") val steps: List<SimpleStepDto>, // 시간 필드 없는 Step
    @SerializedName("selectedApps") val selectedApps: List<String>?, // 간편에도 보낼거면 유지, 아니면 null로
    @SerializedName("isSimple") val isSimple: Boolean,
    @SerializedName("isUserVisible") val isUserVisible: Boolean
)

// [신규] 간편 루틴 Step (시간 없음)
data class SimpleStepDto(
    @SerializedName("name") val name: String,
    @SerializedName("stepOrder") val stepOrder: Int
)