package com.example.kuitandroidapiexample.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class BaseResponse<T>(
    @SerialName("data")
    val data: T
)
