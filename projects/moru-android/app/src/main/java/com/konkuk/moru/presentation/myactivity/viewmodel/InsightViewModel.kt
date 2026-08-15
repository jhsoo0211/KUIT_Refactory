package com.konkuk.moru.presentation.myactivity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.domain.model.Insight
import com.konkuk.moru.domain.repository.InsightRepository
import com.konkuk.moru.presentation.login.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "InsightVM"

data class InsightUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val paceGrade: String = "",
    val routineCompletionRate: Double = 0.0,                 // Double
    val globalAverageRoutineCompletionRate: Double = 0.0,    // Double

    val completionDistribution: Map<String, Int> = emptyMap(), // Map<String,Int>
    val weekdayUser: Double = 0.0,                             // Double
    val weekdayOverall: Double = 0.0,                          // Double
    val weekendUser: Double = 0.0,                             // Double
    val weekendOverall: Double = 0.0,                          // Double
    val completionByTimeSlot: Map<String, Int> = emptyMap()    // Map<String,Int>
)

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val insightRepository: InsightRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _ui = MutableStateFlow(InsightUiState())
    val ui: StateFlow<InsightUiState> = _ui

    init {
        viewModelScope.launch {
            userSessionManager.isLoggedIn.collect { loggedIn ->
                Log.d(TAG, "isLoggedIn emit = $loggedIn")
                if (loggedIn) loadInsights()
            }
        }
    }

    private fun loadInsights() {
        Log.d(TAG, "loadInsights() called")
        _ui.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            insightRepository.getInsights()
                .onSuccess { insight ->
                    Log.d(TAG, "getInsights() success")
                    _ui.update { it.merge(insight) }
                }
                .onFailure { e ->
                    Log.e(TAG, "getInsights() failure: exception=${e.javaClass.simpleName}")
                    _ui.update { it.copy(isLoading = false, error = e.message ?: "알 수 없는 오류") }
                }
        }
    }
    
    // 간편 루틴 완료 시 실천율 업데이트
    fun completeRoutine(routineId: String) {
        Log.d(TAG, "completeRoutine() called")
        viewModelScope.launch {
            insightRepository.completeRoutine(routineId)
                .onSuccess {
                    Log.d(TAG, "completeRoutine() success")
                    // 실천율 정보 새로고침
                    loadInsights()
                }
                .onFailure { e ->
                    Log.e(TAG, "completeRoutine() failure: exception=${e.javaClass.simpleName}")
                }
        }
    }

    private fun InsightUiState.merge(src: Insight) = copy(
        isLoading = false,
        error = null,
        paceGrade = src.paceGrade,
        routineCompletionRate = src.routineCompletionRate,
        globalAverageRoutineCompletionRate = src.globalAverageRoutineCompletionRate,
        completionDistribution = src.completionDistribution,
        weekdayUser = src.weekdayUser,
        weekdayOverall = src.weekdayOverall,
        weekendUser = src.weekendUser,
        weekendOverall = src.weekendOverall,
        completionByTimeSlot = src.completionByTimeSlot
    )
}
