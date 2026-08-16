package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.Follow.FollowUserDto
import com.konkuk.moru.presentation.routinefeed.data.FollowUser


fun FollowUserDto.toUi() = FollowUser(
    id = userId,
    profileImageUrl = profileImageUrl ?: "",
    username = nickname,
    bio = bio ?: "",
    isFollowing = isFollow
)