package com.konkuk.moru.data.dto.error

import com.google.gson.annotations.SerializedName

/**
 * 서버 에러 바디가 두 가지 계열로 내려오는 걸 모두 포용하기 위한 래퍼.
 * 1) 기본 스프링: timestamp, status, error, path
 * 2) 커스텀: status, code, message
 */
data class ErrorEnvelope(
    // 공통 혹은 커스텀
    @SerializedName("status") val status: Int? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("message") val message: String? = null,

    // 스프링 기본
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("path") val path: String? = null,
)