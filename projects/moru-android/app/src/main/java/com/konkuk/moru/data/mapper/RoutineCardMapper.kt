package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.data.model.RoutineCardDomain
import com.konkuk.moru.core.datastore.SocialMemory

/**
 * Domain → UI 모델 매핑
 * UI에서 사용하는 Routine(피드 카드용)으로 가볍게 채워 넣습니다.
 * authorId는 프로필 주인 id로 세팅(상세로 넘어갈 때 작성자 프로필 이동 등에 사용 가능)
 */


fun RoutineCardDomain.toUiRoutine(
    profileOwnerId: String,
    authorName: String,
    authorProfileUrl: String?
): Routine {
    // [추가] 전역 메모리 조회
    val mem = SocialMemory.getRoutine(id)

    return Routine(
        routineId = id,
        title = title,
        imageUrl = imageUrl,
        tags = tags,

        // [변경] 카드에도 최신 likeCount 반영
        likes = mem?.likeCount ?: likeCount,
        isRunning = isRunning,

        description = "",
        category = "일상",
        authorId = profileOwnerId ?: "",
        authorName = authorName,
        authorProfileUrl = authorProfileUrl,

        // [변경] 초기 상태도 메모리 우선
        isLiked = mem?.isLiked ?: false,          // ★ 기존: false 고정 → 수정
        isBookmarked = mem?.isScrapped ?: false,  // ★ 기존: false 고정 → 수정

        isChecked = false,
        scheduledTime = null,
        scheduledDays = emptySet(),
        isAlarmEnabled = false,
        steps = emptyList(),
        similarRoutines = emptyList(),
        usedApps = emptyList()
    )
}