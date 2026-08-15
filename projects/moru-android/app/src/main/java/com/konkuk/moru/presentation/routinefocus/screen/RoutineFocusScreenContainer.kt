package com.konkuk.moru.presentation.routinefocus.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.konkuk.moru.presentation.routinefocus.viewmodel.RoutineFocusViewModel
import com.konkuk.moru.presentation.routinefocus.viewmodel.SharedRoutineViewModel

@Composable
fun RoutineFocusScreenContainer(
    focusViewModel: RoutineFocusViewModel,
    sharedViewModel: SharedRoutineViewModel,
    onDismiss: () -> Unit,
    onFinishConfirmed: (String) -> Unit,
    onNavigateToMyActivity: () -> Unit = {}
){
    val selectedIdState by sharedViewModel.selectedRoutineId.collectAsState(initial = null)
    val selectedId: Int = selectedIdState ?: 0

    if (focusViewModel.isLandscapeMode) {
        LandscapeRoutineFocusScreen(
            focusViewModel = focusViewModel,
            sharedViewModel = sharedViewModel,
            routineId = selectedId,
            onDismiss = onDismiss,
            currentStep = focusViewModel.currentStep,
            onFinishConfirmed = onFinishConfirmed,
            forceShowFinishPopup = false,
            forceShowResultPopup = false,
            onNavigateToMyActivity = onNavigateToMyActivity
        )
    } else {
        PortraitRoutineFocusScreen(
            focusViewModel = focusViewModel,
            sharedViewModel = sharedViewModel,
            routineId = selectedId,
            onDismiss = onDismiss,
            currentStep = focusViewModel.currentStep,
            onFinishConfirmed = onFinishConfirmed,
            forceShowFinishPopup = false,
            forceShowResultPopup = false,
            onNavigateToMyActivity = onNavigateToMyActivity
        )
    }

}