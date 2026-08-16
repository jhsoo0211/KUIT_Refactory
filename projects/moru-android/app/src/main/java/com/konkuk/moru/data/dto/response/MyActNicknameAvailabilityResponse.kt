package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MyActNicknameAvailabilityResponse(
    val available: Boolean
)
