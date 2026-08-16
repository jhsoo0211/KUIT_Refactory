package com.konkuk.moru.data.dto.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    val nickname: String,
    val gender: String,
    @SerialName("birthDate")
    @SerializedName("birthDate")
    val birthday: String,
    val bio: String,
    val profileImageUrl: String? = null,
    @SerialName("imageKey")
    @SerializedName("imageKey")
    val imageKey: String? = null
)
