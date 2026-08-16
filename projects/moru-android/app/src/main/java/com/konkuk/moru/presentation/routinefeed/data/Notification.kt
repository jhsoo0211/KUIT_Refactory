package com.konkuk.moru.presentation.routinefeed.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime


data class Notification(
    val id: String,
    val senderId: String,
    val userImageUrl: String?,
    val actingUser: String, // 행동을 한 사용자 (예: "xx님")
    val messageAction: String, // 행동 (예: "을 팔로우 했습니다.", "[루틴명]을 생성했습니다.")
    val targetName: String?, // 행동의 대상 (예: "사용자명") -> 대상이 없는 경우도 있으므로 Nullable
    val timestamp: LocalDateTime
)

data class NotificationItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("senderNickname") val senderNickname: String,
    @SerializedName("senderProfileImage") val senderProfileImage: String?,
    @SerializedName("message") val message: String,
    @SerializedName("relativeTime") val relativeTime: String
)

data class NotificationCursorDto(
    @SerializedName("createdAt") val createdAt: String, // ISO-8601
    @SerializedName("id") val id: String
)

data class NotificationListResponseDto(
    @SerializedName("content") val content: List<NotificationItemDto>,
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("nextCursor") val nextCursor: NotificationCursorDto?
)