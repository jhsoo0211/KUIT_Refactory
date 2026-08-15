package com.konkuk.moru.data.dto.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)