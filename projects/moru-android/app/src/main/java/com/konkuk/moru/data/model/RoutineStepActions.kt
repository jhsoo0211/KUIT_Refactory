package com.konkuk.moru.data.model

import androidx.compose.runtime.Immutable

@Immutable // Composable이 이 객체를 안정적으로 취급하여 불필요한 리컴포지션을 방지
data class RoutineStepActions(
    val onDragStart: (Int) -> Unit,
    val onDrag: (Float) -> Unit,
    val onReorderComplete: (from: Int, to: Int) -> Unit, // 성공 시
    val onReorderCancel: () -> Unit,                     // 취소 시
    val onDeleteStep: (Int) -> Unit,
    val onStepNameChange: (index: Int, newName: String) -> Unit,
    val onAddStep: () -> Unit,
    val onTimeClick: (Int) -> Unit
)
