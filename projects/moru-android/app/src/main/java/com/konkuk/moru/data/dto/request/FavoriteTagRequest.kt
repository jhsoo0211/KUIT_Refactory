package com.konkuk.moru.data.dto.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteTagRequest(
    @SerialName("tagIds")
    @SerializedName("tagIds")
    val tagIds: List<String>
)
