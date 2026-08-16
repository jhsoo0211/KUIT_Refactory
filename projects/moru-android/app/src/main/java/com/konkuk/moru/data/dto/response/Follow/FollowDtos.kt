package com.konkuk.moru.data.dto.response.Follow

import com.google.gson.annotations.SerializedName

data class FollowListResponseDto(
    @SerializedName("content") val content: List<FollowUserDto>,
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("nextCursor") val nextCursor: FollowCursorDto?
)

data class FollowUserDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("bio") val bio: String?,
    @SerializedName("isFollow") val isFollow: Boolean
)

data class FollowCursorDto(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("userId") val userId: String
)