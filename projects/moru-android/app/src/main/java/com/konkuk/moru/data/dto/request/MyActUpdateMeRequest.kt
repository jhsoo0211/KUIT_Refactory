package com.konkuk.moru.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class MyActUpdateMeRequest(
    val nickname: String,
    val gender: String,          // "MALE" | "FEMALE"
    val birthday: String,        // "yyyy-MM-dd"
    val bio: String,
    val profileImageUrl: String? = null
)
