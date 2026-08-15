package com.konkuk.moru.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val fcmToken: String
)