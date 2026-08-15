package com.konkuk.moru.domain.model

data class MyActRecordDetail(
    val id: String,
    val title: String,
    val isSimple: Boolean,
    val isCompleted: Boolean,
    val startedAtIso: String,
    val endedAtIso: String?,
    val totalSec: Long,
    val imageUrl: String,
    val tags: List<String>,
    val steps: List<MyActRecordDetailStep>,
    val apps: List<MyActRecordDetailApp>,
    val completedStepCount: Int,
    val totalStepCount: Int
)

data class MyActRecordDetailStep(
    val order: Int,
    val name: String,
    val note: String?,
    val estimatedSec: Long,
    val actualSec: Long,
    val isCompleted: Boolean
)

data class MyActRecordDetailApp(
    val packageName: String,
    val name: String
)
