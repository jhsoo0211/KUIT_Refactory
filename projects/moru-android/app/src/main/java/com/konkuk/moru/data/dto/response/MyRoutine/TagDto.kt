package com.konkuk.moru.data.dto.response.MyRoutine

import kotlinx.serialization.Serializable


@Serializable
data class TagDto(
    val id: String,
    val name: String,
    val createdAt: String? = null // 스웨거 스펙 포함(옵션)
)