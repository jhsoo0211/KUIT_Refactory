package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineDetailDto
import com.konkuk.moru.data.model.MyRoutineDetailUi
import com.konkuk.moru.data.model.RoutineStep
import com.konkuk.moru.data.model.UsedAppInRoutine
import com.konkuk.moru.data.model.placeholderIcon

// [추가] Dto → 화면용 Routine
// "PT5M" -> "00:05:00"
private fun String?.isoToHms(): String {
    if (this.isNullOrBlank()) return "00:00:00"
    // 간단 변환: PT1H30M15S 형태 처리
    var h=0; var m=0; var s=0
    val body = this.removePrefix("PT")
    Regex("(\\d+)H").find(body)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { h = it }
    Regex("(\\d+)M").find(body)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { m = it }
    Regex("(\\d+)S").find(body)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { s = it }
    return "%02d:%02d:%02d".format(h, m, s)
}

fun MyRoutineDetailDto.toMyDetailUi(): MyRoutineDetailUi {
    val category = if (isSimple) "간편" else "집중"

    val uiSteps = steps
        .sortedBy { it.stepOrder }
        .map { s -> RoutineStep(name = s.name, duration = s.estimatedTime.isoToHms()) }

    val uiApps = apps.map { a ->
        UsedAppInRoutine(
            appName = a.name,
            appIcon = placeholderIcon(),  // 서버에 앱 아이콘 없으므로 플레이스홀더
            packageName = a.packageName
        )
    }

    return MyRoutineDetailUi(
        routineId = id,
        title = title,
        description = description ?: "",
        imageUrl = imageUrl,
        category = category,
        tags = tags,
        authorId = author.id,
        authorName = author.nickname,
        authorProfileUrl = author.profileImageUrl,
        likes = likeCount,
        isLiked = isLiked,
        isBookmarked = isScrapped,      // 서버 필드명과 맞춤
        isRunning = false,
        isUserVisible = this.isUserVisible ?: false,
        isSimple = isSimple,
        isChecked = false,
        scrapCount = scrapCount,
        steps = uiSteps,
        usedApps = uiApps
    )
}