package com.example.kuitandroidapiexample.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAnimalDto(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("state") val state: String,
    @SerialName("breed") val breed: String,
    @SerialName("address") val address: String
)