package com.konkuk.moru.data.dto.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.EncodeDefault.Mode.ALWAYS
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// [신규] 루틴 피드 상세 → 내 루틴으로 복제 생성용 Request DTO
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class RoutineFeedCreateRequest (
    val title: String,

    @EncodeDefault(ALWAYS)
    val imageKey: String? = null,

    @EncodeDefault(ALWAYS)
    val tags: List<String> = emptyList(),

    val description: String? = null,

    @EncodeDefault(ALWAYS)
    val steps: List<Step>,

    // ← 서버 키와 맞추기
    @SerialName("apps")
    @EncodeDefault(ALWAYS)
    val selectedApps: List<String> = emptyList(),

    @EncodeDefault(ALWAYS)
    val isSimple: Boolean,

    @EncodeDefault(ALWAYS)
    val isUserVisible: Boolean = true
) {
    @Serializable
    data class Step(
        val name: String,
        val stepOrder: Int,
        val estimatedTime: String              // ISO-8601 e.g. "PT5M"
    )
}