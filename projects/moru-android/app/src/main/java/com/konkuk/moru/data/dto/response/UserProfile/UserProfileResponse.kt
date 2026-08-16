package com.konkuk.moru.data.dto.response.UserProfile

import kotlinx.serialization.Serializable

// @SerializedName은 안적어두겠습니당
@Serializable
data class UserProfileResponse(
    // 회원가입시 작성한 이메일
    val id: String,

    // 회원가입시 작성한 닉네임
    val nickname: String,

    // 성별
    val gender: String,

    // 생년월일
    val birthday: String,

    // 자기소개
    val bio: String,

    // 프로필 이미지
    val profileImageUrl: String?,

    // 내 루틴 갯수
    val routineCount: Int,

    // 팔로워 수
    val followerCount: Int,

    // 팔로우 수
    val followingCount: Int
)