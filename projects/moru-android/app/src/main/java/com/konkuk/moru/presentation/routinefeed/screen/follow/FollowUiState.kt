package com.konkuk.moru.presentation.routinefeed.screen.follow


import com.konkuk.moru.presentation.routinefeed.data.FollowUser

data class FollowUiState(
    val ownerName: String = "",

    // ✅ 내 사용자 ID (자기 자신 버튼 숨김용)
    val myId: String? = null,

    // ✅ 목록
    val followers: List<FollowUser> = emptyList(),
    val followings: List<FollowUser> = emptyList(),

    // ✅ 로딩/요청중 표시
    val isLoadingFollowers: Boolean = false,
    val isLoadingFollowings: Boolean = false,

    // ✅ 사용자별 in-flight 가드(요청중이면 버튼 비활성화)
    val inFlight: Set<String> = emptySet()
)

data class FollowRelation(
    val followerId: String, // 팔로우를 하는 사람 ID
    val followingId: String  // 팔로우를 당하는 사람 ID
)
