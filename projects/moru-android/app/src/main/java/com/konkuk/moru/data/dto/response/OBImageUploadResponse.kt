package com.konkuk.moru.data.dto.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OBImageUploadResponse(
    @SerialName("key")        @SerializedName("key")        val key: String? = null,
    @SerialName("imageKey")   @SerializedName("imageKey")   val imageKey: String? = null,
    @SerialName("path")       @SerializedName("path")       val path: String? = null,
    @SerialName("url")        @SerializedName("url")        val url: String? = null,
    @SerialName("imageUrl")   @SerializedName("imageUrl")   val imageUrl: String? = null,
    @SerialName("result")     @SerializedName("result")     val result: String? = null,
    @SerialName("data")       @SerializedName("data")       val data: Data? = null
) {
    @Serializable
    data class Data(
        @SerialName("key")      @SerializedName("key")      val key: String? = null,
        @SerialName("imageKey") @SerializedName("imageKey") val imageKey: String? = null,
        @SerialName("path")     @SerializedName("path")     val path: String? = null,
        @SerialName("url")      @SerializedName("url")      val url: String? = null,
        @SerialName("imageUrl") @SerializedName("imageUrl") val imageUrl: String? = null,
        @SerialName("result")   @SerializedName("result")   val result: String? = null,
    )
    fun bestKeyOrNull(): String? =
        imageUrl ?: key ?: imageKey ?: path ?: url
        ?: data?.imageUrl ?: data?.key ?: data?.imageKey ?: data?.path ?: data?.url
        ?: result ?: data?.result
}