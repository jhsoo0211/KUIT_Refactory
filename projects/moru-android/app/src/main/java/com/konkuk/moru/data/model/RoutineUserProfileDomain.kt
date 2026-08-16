package com.konkuk.moru.data.model


data class RoutineUserProfileDomain(
    val id: String,
    val isMe: Boolean,
    val nickname: String,
    val profileImageUrl: String?,
    val bio: String?,
    val routineCount: Int,
    val followerCount: Int,
    val followingCount: Int,
    val currentRoutine: RoutineCardDomain?,
    val routines: List<RoutineCardDomain>
)

data class RoutineCardDomain(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val tags: List<String>,
    val likeCount: Int,
    val createdAt: String?,
    val requiredTime: String?,
    val isRunning:Boolean
)