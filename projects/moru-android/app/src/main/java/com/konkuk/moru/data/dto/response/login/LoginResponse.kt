package com.konkuk.moru.data.dto.response.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: Token,

    @SerialName("isOnboarding")
    val isonboarding: Boolean
)

@Serializable
data class Token(
    val accessToken: String,
    val refreshToken: String
)

