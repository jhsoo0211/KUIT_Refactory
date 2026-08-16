package com.konkuk.kuitproject1.data.dto.respose

import kotlinx.serialization.Serializable

@Serializable
data class ResposeHomeDto(
    val homeTitle:String,
    val homeSubTitle:String,
    val homeImage:Int
)
