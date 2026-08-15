package com.konkuk.moru.data.model

data class MyRoutineDetailUiState(
    val routine:  MyRoutineDetailUi? = null,
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,
    val draggedStepIndex: Int? = null,
    val draggedStepVerticalOffset: Float = 0f,
    val isQuickMode: Boolean = false,
)