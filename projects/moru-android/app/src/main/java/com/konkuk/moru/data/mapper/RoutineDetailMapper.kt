package com.konkuk.moru.data.mapper


import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ImageBitmap
import com.konkuk.moru.core.datastore.SocialMemory
import com.konkuk.moru.core.util.getAppIconImageBitmap
import com.konkuk.moru.core.util.getAppLabel
import com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponse
import com.konkuk.moru.data.model.AppInfo
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.data.model.RoutineStep
import com.konkuk.moru.data.model.SimilarRoutine
import com.konkuk.moru.data.model.UsedAppInRoutine
import com.konkuk.moru.data.model.placeholderIcon
import com.konkuk.moru.presentation.routinefeed.data.AppDto

import com.konkuk.moru.presentation.routinefeed.data.RoutineStepDto
import com.konkuk.moru.presentation.routinefeed.data.SimilarRoutineItemDto
import java.time.Duration

// RoutineDetailResponse -> Routine (앱 공용 모델)
private fun firstNonBlank(vararg s: String?): String? =
    s.firstOrNull { !it.isNullOrBlank() }

// RoutineDetailResponse -> Routine
fun RoutineDetailResponse.toRoutineModel(
    prev: Routine? = null,
    pm: PackageManager? = null
): Routine {
    val base = prev ?: emptyRoutine(id)

    val resolvedAuthorId = firstNonBlank(author?.id, base.authorId) ?: ""
    val resolvedAuthorName = firstNonBlank(author?.nickname, base.authorName, "알 수 없음") ?: "알 수 없음"
    val resolvedAuthorProfile = firstNonBlank(author?.profileImageUrl, base.authorProfileUrl)

    // [추가] 전역 메모리 오버레이(최신 사용자 동작 우선)
    val mem = SocialMemory.getRoutine(id)

    val finalLikeCount = mem?.likeCount ?: this.likeCount
    val finalIsLiked = mem?.isLiked ?: (this.isLiked ?: base.isLiked)

    val finalScrapCount = mem?.scrapCount ?: this.scrapCount
    val finalIsScrapped = mem?.isScrapped ?: (this.isScrapped ?: base.isBookmarked)

    return base.copy(
        routineId = id,
        title = firstNonBlank(title, base.title) ?: "",
        description = firstNonBlank(description, base.description) ?: "",
        imageUrl = firstNonBlank(imageUrl, base.imageUrl),
        category = if (isSimple) "간편" else "집중",
        tags = tags.orEmpty().ifEmpty { base.tags },

        // [변경] 서버값 대신 오버레이 결과 사용
        likes = finalLikeCount,
        isLiked = finalIsLiked,
        isBookmarked = finalIsScrapped,
        scrapCount = finalScrapCount,

        steps = steps.orEmpty()
            .sortedBy { it.stepOrder }
            .map { it.toStepModel() },
        usedApps = apps.orEmpty().map { it.toUsedApp(pm) },

        authorId = resolvedAuthorId,
        authorName = resolvedAuthorName,
        authorProfileUrl = resolvedAuthorProfile
    )
}

private fun emptyRoutine(routineId: String) = Routine(
    routineId = routineId,
    title = "",
    imageUrl = null,
    tags = emptyList(),
    likes = 0,


    description = "",
    category = "일상",
    authorId = "",
    authorName = "",
    authorProfileUrl = null,

    isLiked = false,
    isBookmarked = false,
    isRunning = false,
    isChecked = false,
    scheduledTime = null,
    scheduledDays = emptySet(),
    isAlarmEnabled = false,

    steps = emptyList(),
    similarRoutines = emptyList(),
    usedApps = emptyList()
)


// SimilarRoutineItemDto -> UI용 SimilarRoutine
fun SimilarRoutineItemDto.toUiModel(): SimilarRoutine {
    val picked = when {
        !tag.isNullOrBlank() -> tag
        !tags.isNullOrEmpty() -> tags.first()
        else -> null
    }
    return SimilarRoutine(
        id = id,
        imageUrl = imageUrl,
        name = title,
        tag = picked?.let { "#$it" } ?: "#루틴"
    )
}

private fun RoutineStepDto.toStepModel(): RoutineStep {
    return RoutineStep(
        name = name,
        duration = parseIsoDurationToClock(estimatedTime) // "PT5M" -> "05:00"
    )
}

private fun AppDto.toAppModel(): AppInfo {
    return AppInfo(
        name = name,
        packageName = packageName
    )
}

private fun AppDto.toUsedApp(pm: PackageManager?): UsedAppInRoutine {
    val icon: ImageBitmap =
        pm?.let { getAppIconImageBitmap(it, packageName) } ?: placeholderIcon(96)
    val label: String =
        pm?.let { getAppLabel(it, packageName) } ?: name
    return UsedAppInRoutine(
        appName = label,
        appIcon = icon,
        packageName = packageName
    )
}


// "PT50M" / "PT5M30S" -> "MM:SS" (예: 50:00 / 05:30)
private fun parseIsoDurationToClock(iso: String?): String {
    if (iso.isNullOrBlank()) return "00:00"
    return runCatching {
        val d = Duration.parse(iso)
        val totalSec = d.seconds
        val minutes = totalSec / 60
        val seconds = totalSec % 60
        String.format("%02d:%02d", minutes, seconds)
    }.getOrElse { "00:00" }
}