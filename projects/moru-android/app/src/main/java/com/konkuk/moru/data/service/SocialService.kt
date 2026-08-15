package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.Follow.FollowListResponseDto
import retrofit2.http.*

interface SocialService {

    // 팔로잉 조회
    @GET("api/social/{userId}/following")
    suspend fun getFollowing(
        @Path("userId") userId: String,
        @Query("lastNickname") lastNickname: String? = null,
        @Query("lastUserId") lastUserId: String? = null,
        @Query("limit") limit: Int = 10
    ): FollowListResponseDto

    // 팔로워 조회
    @GET("api/social/{userId}/follower")
    suspend fun getFollowers(
        @Path("userId") userId: String,
        @Query("lastNickname") lastNickname: String? = null,
        @Query("lastUserId") lastUserId: String? = null,
        @Query("limit") limit: Int = 10
    ): FollowListResponseDto

    // 팔로우
    @POST("api/social/following/{userId}")
    suspend fun follow(@Path("userId") targetUserId: String)

    // 언팔로우
    @DELETE("api/social/following/{userId}")
    suspend fun unfollow(@Path("userId") targetUserId: String)
}