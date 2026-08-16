package com.konkuk.moru.data.dto.response

import com.google.gson.annotations.SerializedName

data class CreateRoutineResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("createdAt") val createdAt: String
)
