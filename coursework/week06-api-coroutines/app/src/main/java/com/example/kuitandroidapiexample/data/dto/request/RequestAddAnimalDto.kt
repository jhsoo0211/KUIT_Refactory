package com.example.kuitandroidapiexample.data.dto.request

import com.example.kuitandroidapiexample.model.AnimalType
import kotlinx.serialization.Serializable

@Serializable
data class RequestAddAnimalDto(
    val id: Int,
    val url: String,
    val name: String,
    val state: AnimalType,
    val breed: String,
    val address: String
)