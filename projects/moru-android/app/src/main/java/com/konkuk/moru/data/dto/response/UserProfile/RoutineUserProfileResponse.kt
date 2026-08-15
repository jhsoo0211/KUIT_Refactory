package com.konkuk.moru.data.dto.response.UserProfile

import com.google.gson.annotations.SerializedName

data class RoutineUserProfileResponse(
    @SerializedName("isMe") val isMe: Boolean,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("routineCount") val routineCount: Int,
    @SerializedName("followerCount") val followerCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
    @SerializedName("currentRoutine") val currentRoutine: RoutineSummaryDto?,
    @SerializedName("routines") val routines: List<RoutineSummaryDto>
)

data class RoutineSummaryDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("requiredTime") val requiredTime: String?,
    @SerializedName("isRunning") val isRunning:Boolean,
)