package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.response.Follow.FollowListResponseDto
import com.konkuk.moru.data.service.SocialService
import com.konkuk.moru.domain.repository.SocialRepository

import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor(
    private val service: SocialService
) : SocialRepository {
    override suspend fun getFollowers(
        userId: String,
        lastNickname: String?,
        lastUserId: String?,
        limit: Int
    ): FollowListResponseDto =
        service.getFollowers(userId, lastNickname, lastUserId, limit)

    override suspend fun getFollowing(
        userId: String,
        lastNickname: String?,
        lastUserId: String?,
        limit: Int
    ): FollowListResponseDto =
        service.getFollowing(userId, lastNickname, lastUserId, limit)

    override suspend fun follow(userId: String) = service.follow(userId)

    override suspend fun unfollow(userId: String) = service.unfollow(userId)
}