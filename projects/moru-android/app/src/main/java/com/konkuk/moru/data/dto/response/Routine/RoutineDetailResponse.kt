package com.konkuk.moru.data.dto.response.Routine

import com.google.gson.annotations.SerializedName
import com.konkuk.moru.data.dto.response.RoutineStepResponse
import com.konkuk.moru.presentation.routinefeed.data.AppDto
import com.konkuk.moru.presentation.routinefeed.data.AuthorDto
import com.konkuk.moru.presentation.routinefeed.data.RoutineStepDto
import com.konkuk.moru.presentation.routinefeed.data.SimilarRoutineItemDto
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

data class RoutineDetailResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("author") val author: AuthorDto?,
    @SerializedName("tags") val tags: List<String>?=null,
    @SerializedName("description") val description: String,
    @SerializedName("isSimple") val isSimple: Boolean,
    @SerializedName("isUserVisible") val isUserVisible: Boolean,
    @SerializedName("steps") val steps: List<RoutineStepDto>?=null,
    @SerializedName("apps") val apps: List<AppDto>?=null,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("requiredTime") val requiredTime: String?=null, // ISO-8601 Duration (e.g., PT50M)
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("isLiked") val isLiked: Boolean? = null,
    @SerializedName("scrapCount") val scrapCount: Int,
    @SerializedName("isScrapped") val isScrapped: Boolean? = null,
    @SerializedName("isOwner") val isOwner: Boolean,
    @SerializedName("similarRoutines") val similarRoutines: List<SimilarRoutineItemDto>?=null,
)


// 아래는 현승이가 한거 오류 나서 V1만 붙임
@Serializable
@JsonIgnoreUnknownKeys
data class RoutineDetailResponseV1(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val category: String? = null,
    val tags: List<String> = emptyList(),
    val likeCount: Int = 0,
    val createdAt: String = "",
    val requiredTime: String = "",
    val isRunning: Boolean = false,
    val scheduledDays: List<String> = emptyList(),
    val scheduledTime: String? = null,
    val steps: List<RoutineStepResponse> = emptyList(),
    val author: AuthorResponse? = null,
    val authorId: String? = null,
    val authorName: String? = null,
    val authorEmail: String? = null,
    val authorProfileImageUrl: String? = null
)

@Serializable
@JsonIgnoreUnknownKeys
data class AuthorResponse(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val profileImageUrl: String? = null
)