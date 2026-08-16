package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class NicknameCheckResponse(val available: Boolean)