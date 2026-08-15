package com.konkuk.moru.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileResponse(
    val id: String,
    val nickname: String,
    val gender: String,
    val birthday: String,
    val bio: String,
    val profileImageUrl: String?,
    val routineCount: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0
)
