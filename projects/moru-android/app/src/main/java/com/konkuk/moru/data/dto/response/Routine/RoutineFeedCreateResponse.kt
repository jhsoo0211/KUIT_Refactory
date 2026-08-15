package com.konkuk.moru.data.dto.response.Routine

import kotlinx.serialization.Serializable



// [신규] 루틴 생성 응답 DTO(피드 기원임을 명시)
@Serializable
data class RoutineFeedCreateResponse(
    val id: String,
    val title: String,
    val createdAt: String? = null
)