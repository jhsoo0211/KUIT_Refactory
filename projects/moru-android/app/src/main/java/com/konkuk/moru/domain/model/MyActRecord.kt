package com.konkuk.moru.domain.model

import java.time.LocalDate

data class MyActRecord(
    val id: String,
    val title: String,
    val tags: List<String>,
    val isCompleted: Boolean,
    val imageUrl: String,
    val durationSec: Long,
    val startedAt: LocalDate
)
