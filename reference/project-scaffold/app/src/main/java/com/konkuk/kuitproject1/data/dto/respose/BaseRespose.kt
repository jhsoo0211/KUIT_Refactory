package com.konkuk.kuitproject1.data.dto.respose

import kotlinx.serialization.Serializable


@Serializable
data class BaseRespose<T>(
    val code:Int,
    val data:T,
    val message:String
)
