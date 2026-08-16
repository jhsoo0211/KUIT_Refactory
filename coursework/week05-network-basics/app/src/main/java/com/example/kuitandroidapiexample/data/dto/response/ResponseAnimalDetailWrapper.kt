package com.example.kuitandroidapiexample.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAnimalDetailWrapper(
    @SerialName("data") val data: ResponseAnimalDetailDto
)