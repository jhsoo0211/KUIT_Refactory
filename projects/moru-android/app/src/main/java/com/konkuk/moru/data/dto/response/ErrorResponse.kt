package com.konkuk.moru.data.dto.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("message") val message: String? = null,

    // 스프링 기본 에러 필드도 겸사겸사 받자 (서버가 상황에 따라 이 형태를 줄 수도 있으니)
    @SerializedName("error") val error: String? = null,
    @SerializedName("path") val path: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null
)
