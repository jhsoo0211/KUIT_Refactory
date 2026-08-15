package com.example.kuitandroidapiexample.data.dto.response

import com.example.kuitandroidapiexample.ui.model.AnimalType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAnimalDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
    @SerialName("state")
    val state: AnimalType,
    @SerialName("breed")
    val breed: String,
    @SerialName("address")
    val address: String
)
