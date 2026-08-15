package com.konkuk.moru.data.mapper


import com.konkuk.moru.data.dto.response.UserProfile.RoutineSummaryDto
import com.konkuk.moru.data.dto.response.UserProfile.RoutineUserProfileResponse
import com.konkuk.moru.data.model.RoutineCardDomain
import com.konkuk.moru.data.model.RoutineUserProfileDomain
import com.konkuk.moru.presentation.routinefeed.data.UserMeResponse

fun UserMeResponse.toDomain(): RoutineUserProfileDomain =
    RoutineUserProfileDomain(
        id = id,
        isMe = true,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        bio = bio,
        routineCount = routineCount,
        followerCount = followerCount,
        followingCount = followingCount,
        currentRoutine = null,
        routines = emptyList()
    )

fun RoutineUserProfileResponse.toDomain(userId: String): RoutineUserProfileDomain =
    RoutineUserProfileDomain(
        id = userId,
        isMe = isMe,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        bio = bio,
        routineCount = routineCount,
        followerCount = followerCount,
        followingCount = followingCount,
        currentRoutine = currentRoutine?.toDomain(),
        routines = routines.map { it.toDomain() }
    )

fun RoutineSummaryDto.toDomain() = RoutineCardDomain(
    id = id,
    title = title,
    imageUrl = imageUrl,
    tags = tags,
    likeCount = likeCount,
    createdAt = createdAt ?: "",
    requiredTime = requiredTime,
    isRunning = isRunning
)