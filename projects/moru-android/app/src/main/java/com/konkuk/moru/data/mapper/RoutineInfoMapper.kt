package com.konkuk.moru.data.mapper

import com.konkuk.moru.core.datastore.LikeMemory
import com.konkuk.moru.core.datastore.SocialMemory
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.presentation.routinefeed.data.RoutineInfo


fun RoutineInfo.toRoutineModel(
    authorIdFallback: String? = null,
    authorNameFallback: String? = null,
    authorProfileUrlFallback: String? = null
): Routine {

    val mem = SocialMemory.getRoutine(this.id)
    return Routine(
        routineId = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        tags = this.tags,
        likes = mem?.likeCount ?: likeCount,
        isRunning = this.isRunning,
        isLiked = mem?.isLiked ?: false,

        description = "Description not available",
        category = "일상",

        // [변경] null 고정 → fallback 사용
        authorId = authorIdFallback ?: "",
        authorName = authorNameFallback ?: "Unknown",
        authorProfileUrl = authorProfileUrlFallback, // ✅ 이제 값이 들어올 수 있음

        isBookmarked = mem?.isScrapped ?: false,
        isChecked = false,
        scheduledTime = null,
        scheduledDays = emptySet(),
        isAlarmEnabled = false,
        steps = emptyList(),
        similarRoutines = emptyList(),
        usedApps = emptyList()
    )
}