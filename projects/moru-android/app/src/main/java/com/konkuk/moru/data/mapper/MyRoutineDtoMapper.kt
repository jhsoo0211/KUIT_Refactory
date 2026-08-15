package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineDetailDto
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineSummaryDto
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineUi



// [추가] 목록 DTO → UI
fun MyRoutineSummaryDto.toMyUi(): MyRoutineUi = MyRoutineUi(
    routineId = id,
    title = title,
    imageUrl = imageUrl,
    tags = tags,
    likes = likeCount,
    isLiked = false,          // 목록엔 isLiked 없음
    isRunning = isRunning,
    scheduledTime = null,
    scheduledDays = emptySet(),
    isAlarmEnabled = false,
    isChecked = false,
    authorId = null,
    createdAt = createdAt // ▼ 추가

)

// [추가] 상세 DTO → UI(상세 화면에서 사용)
fun MyRoutineDetailDto.toMyUi(): MyRoutineUi = MyRoutineUi(
    routineId = id,
    title = title,
    imageUrl = imageUrl,
    tags = tags,
    likes = likeCount,
    isLiked = isLiked,
    isRunning = false,        // 상세엔 없음
    scheduledTime = null,
    scheduledDays = emptySet(),
    isAlarmEnabled = false,
    isChecked = false,
    authorId = author.id
)