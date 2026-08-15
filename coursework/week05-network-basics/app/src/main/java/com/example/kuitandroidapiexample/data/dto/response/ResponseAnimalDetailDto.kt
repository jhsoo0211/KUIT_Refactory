package com.example.kuitandroidapiexample.data.dto.response



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAnimalDetailDto(
    @SerialName("id")          val id: Int,
    @SerialName("name")        val name: String,
    @SerialName("url")         val url: String,
    @SerialName("state")       val state: String,
    @SerialName("breed")       val breed: String,
    @SerialName("address")     val address: String,
    @SerialName("description") val reporterName: String?= null
)