package com.konkuk.moru.presentation.routinefeed.data

import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("id") val id: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String?
)

data class RoutineStepDto(
    @SerializedName("id") val id: String,
    @SerializedName("stepOrder") val stepOrder: Int,
    @SerializedName("name") val name: String,
    @SerializedName("estimatedTime") val estimatedTime: String // ISO-8601 Duration (e.g., PT5M)
)

data class AppDto(
    @SerializedName("packageName") val packageName: String,
    @SerializedName("name") val name: String
)

data class SimilarRoutineItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("tag") val tag: String? = null,
    @SerializedName("tags") val tags: List<String>?=null,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("requiredTime") val requiredTime: String?
)