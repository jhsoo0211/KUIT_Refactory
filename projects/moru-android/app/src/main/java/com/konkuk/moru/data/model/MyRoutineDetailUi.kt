package com.konkuk.moru.data.model

data class MyRoutineDetailUi(
    // 기본 정보
    val routineId: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val category: String,             // "집중" | "간편"
    val tags: List<String>,

    // 작성자
    val authorId: String,
    val authorName: String?,
    val authorProfileUrl: String?,

    // 상태
    val likes: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,        // 서버 isScrapped 매핑
    val isRunning: Boolean = false,
    val isSimple: Boolean = false,
    val isChecked: Boolean = false,
    val scrapCount: Int = 0,
    val isUserVisible: Boolean = false,
    // 상세
    val steps: List<RoutineStep> = emptyList(),
    val usedApps: List<UsedAppInRoutine> = emptyList()
)

/*
data class MyRoutineDetailUiState(
    // [변경] Routine? -> MyRoutineDetailUi?
    val routine: MyRoutineDetailUi? = null,
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,
    val draggedStepIndex: Int? = null,
    val draggedStepVerticalOffset: Float = 0f,
    val isQuickMode: Boolean = false,
)*/
