package com.konkuk.moru.data.model

import java.util.UUID

data class RoutineStep(
    val id: UUID = UUID.randomUUID(), // 순서 변경을 위한 id 추가
    val name: String,
    val duration: String // "00:00" 형식
)