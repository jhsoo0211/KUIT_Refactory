package com.konkuk.moru.presentation.routinefeed.data

import com.konkuk.moru.data.model.Routine

data class FollowUser(
    val id: String,
    val profileImageUrl: String, // 임시로 비워두거나 기본 이미지 URL 사용
    val username: String,
    val bio: String,
    val isFollowing: Boolean
)


data class UserProfileUiState(
    val userId:String?=null,
    val isMe: Boolean?=false,
    val nickname: String = "",
    val bio: String = "",
    val profileImageUrl: String? = null,
    val routineCount: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean = false,
    val isFollowLoading: Boolean = false,
    val runningRoutines: List<Routine> = emptyList(),
    val userRoutines: List<Routine> = emptyList(),
    val isRunningRoutineExpanded: Boolean = true
)


data class User(
    val userId: String,
    val nickname: String,
    val bio: String,
    val profileImageUrl: String?
)