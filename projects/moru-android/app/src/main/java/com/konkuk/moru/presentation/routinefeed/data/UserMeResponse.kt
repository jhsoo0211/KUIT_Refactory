package com.konkuk.moru.presentation.routinefeed.data

import com.google.gson.annotations.SerializedName

data class UserMeResponse(
    @SerializedName("id") val id: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("gender") val gender: String?, // "MALE"/"FEMALE" 등
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("routineCount") val routineCount: Int,
    @SerializedName("followerCount") val followerCount: Int,
    @SerializedName("followingCount") val followingCount: Int
)