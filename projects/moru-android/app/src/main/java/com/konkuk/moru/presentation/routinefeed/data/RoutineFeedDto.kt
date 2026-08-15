package com.konkuk.moru.presentation.routinefeed.data

import com.google.gson.annotations.SerializedName
import com.konkuk.moru.data.model.Routine

data class RoutineSectionModel(
    val title: String,
    val routines: List<Routine>
)

data class LiveUserInfo(
    @SerializedName("userId") //필요시 userId로
    val userId: String,

    @SerializedName("nickname") // JSON의 "nickname" 필드를 "name" 변수에 매핑
    val nickname: String,

    @SerializedName("motivationTag") // JSON의 "motivationTag" 필드를 "tag" 변수에 매핑
    val motivationTag: String,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String?
)

data class RoutineFeedResponse(
    @SerializedName("hotRoutines") val hotRoutines: List<RoutineInfo>,
    @SerializedName("personalRoutines") val personalRoutines: List<RoutineInfo>,
    @SerializedName("tagPairSection1") val tagPairSection1: TagPairSection,
    @SerializedName("tagPairSection2") val tagPairSection2: TagPairSection
)

// 개별 루틴 정보 (서버 응답 기준)
data class RoutineInfo(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("requiredTime") val requiredTime: String?,
    @SerializedName("isRunning") val isRunning: Boolean
)

// 태그 조합 섹션
data class TagPairSection(
    @SerializedName("tag1") val tag1: String,
    @SerializedName("tag2") val tag2: String,
    @SerializedName("routines") val routines: List<RoutineInfo>
)
