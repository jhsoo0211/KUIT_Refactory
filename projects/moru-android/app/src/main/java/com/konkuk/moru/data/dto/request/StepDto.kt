package com.konkuk.moru.data.dto.request

import com.google.gson.annotations.SerializedName

data class StepDto(
    @SerializedName("name") val name: String,
    @SerializedName("stepOrder") val stepOrder: Int,
    // ✅ 간편 루틴일 때는 null로 보내서 필드 자체를 생략
    @SerializedName("estimatedTime") val estimatedTime: String?
)
