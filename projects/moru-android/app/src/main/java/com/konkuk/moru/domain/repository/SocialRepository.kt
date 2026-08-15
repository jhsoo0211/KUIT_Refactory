package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.dto.response.Follow.FollowListResponseDto


interface SocialRepository {
    suspend fun getFollowers(
        userId: String,
        lastNickname: String?=null,
        lastUserId: String?=null,
        limit: Int
    ): FollowListResponseDto

    suspend fun getFollowing(
        userId: String,
        lastNickname: String?=null,
        lastUserId: String?=null,
        limit: Int =20
    ): FollowListResponseDto

    suspend fun follow(userId: String)
    suspend fun unfollow(userId: String)
}