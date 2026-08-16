package com.konkuk.moru.presentation.routinefocus.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.konkuk.moru.presentation.home.RoutineStepData
import com.konkuk.moru.presentation.routinefeed.data.AppDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalTime

class SharedRoutineViewModel : ViewModel() {
    
    init {
        Log.d("SharedRoutineViewModel", "🚀 SharedRoutineViewModel 생성됨!")
    }
    
    private val _selectedRoutineId = MutableStateFlow<Int?>(null)
    val selectedRoutineId: StateFlow<Int?> = _selectedRoutineId
    fun setSelectedRoutineId(id: Int) {
        _selectedRoutineId.value = id
    }

    // 원래 String 타입의 routineId 저장 (완료 처리용)
    private val _originalRoutineId = MutableStateFlow<String?>(null)
    val originalRoutineId: StateFlow<String?> = _originalRoutineId
    fun setOriginalRoutineId(id: String) {
        _originalRoutineId.value = id
    }

    // 루틴 제목
    private val _routineTitle = MutableStateFlow("")
    val routineTitle: StateFlow<String> = _routineTitle
    fun setRoutineTitle(title: String) {
        _routineTitle.value = title
    }

    // 루틴 카테고리 (집중 / 간편)
    private val _routineCategory = MutableStateFlow("")
    val routineCategory: StateFlow<String> = _routineCategory
    fun setRoutineCategory(category: String) {
        _routineCategory.value = category
    }

    // 총 소요시간 (초 단위)
    private val _totalDuration = MutableStateFlow(0)
    val totalDuration: StateFlow<Int> = _totalDuration
    fun setTotalDuration(duration: Int) {
        Log.d("SharedRoutineViewModel", "🔄 setTotalDuration: ${duration}초")
        _totalDuration.value = duration
    }
    
    // 총 소요시간 업데이트 (실시간으로 누적)
    fun updateTotalDuration(additionalSeconds: Int) {
        val currentDuration = _totalDuration.value
        val newDuration = currentDuration + additionalSeconds
        Log.d("SharedRoutineViewModel", "🔄 updateTotalDuration: ${currentDuration}초 + ${additionalSeconds}초 = ${newDuration}초")
        _totalDuration.value = newDuration
    }

    // 루틴 태그 리스트
    private val _routineTags = MutableStateFlow<List<String>>(emptyList())
    val routineTags: StateFlow<List<String>> = _routineTags
    fun setRoutineTags(tags: List<String>) {
        _routineTags.value = tags
    }

    // 루틴 이미지 URL
    private val _routineImageUrl = MutableStateFlow<String?>(null)
    val routineImageUrl: StateFlow<String?> = _routineImageUrl
    fun setRoutineImageUrl(imageUrl: String?) {
        _routineImageUrl.value = imageUrl
    }

    // 간편 루틴 여부
    private val _isSimple = MutableStateFlow(false)
    val isSimple: StateFlow<Boolean> = _isSimple
    fun setIsSimple(isSimple: Boolean) {
        Log.d("SharedRoutineViewModel", "🔄 setIsSimple: $isSimple")
        _isSimple.value = isSimple
    }

    // 사용앱 리스트 (루틴 생성 시 선택한 앱들)
    private val _selectedApps = MutableStateFlow<List<AppDto>>(emptyList())
    val selectedApps: StateFlow<List<AppDto>> = _selectedApps
    fun setSelectedApps(apps: List<AppDto>) {
        _selectedApps.value = apps
    }

    // 제목, 카테고리, 태그, 간편 루틴 여부 한꺼번에 설정
    fun setRoutineInfo(title: String, category: String, tags: List<String>, isSimple: Boolean = false, imageUrl: String? = null) {
        _routineTitle.value = title
        _routineCategory.value = category
        _routineTags.value = tags
        _isSimple.value = isSimple
        _routineImageUrl.value = imageUrl
    }

    // 알림 시간 및 요일
    private val _scheduledTime = MutableStateFlow<LocalTime?>(null)
    val scheduledTime: StateFlow<LocalTime?> = _scheduledTime

    private val _scheduledDays = MutableStateFlow<Set<DayOfWeek>>(emptySet())
    val scheduledDays: StateFlow<Set<DayOfWeek>> = _scheduledDays

    fun setSchedule(time: LocalTime?, days: Set<DayOfWeek>) {
        _scheduledTime.value = time
        _scheduledDays.value = days
    }

    // 네비게이션 트리거 (카테고리 기반으로 변경)
    private val _startNavigation = MutableStateFlow<String?>(null)
    val startNavigation: StateFlow<String?> = _startNavigation

    fun onStartClick() {
        _startNavigation.value = _routineCategory.value
    }

    fun onNavigationHandled() {
        _startNavigation.value = null
    }

    // 선택된 스텝 리스트
    private val _selectedSteps = MutableStateFlow<List<RoutineStepData>>(emptyList())
    val selectedSteps: StateFlow<List<RoutineStepData>> = _selectedSteps

    fun setSelectedSteps(steps: List<RoutineStepData>) {
        _selectedSteps.value = steps
    }

    // 서버에서 받은 스텝 정보를 RoutineStepData로 변환하여 설정
    fun setStepsFromServer(steps: List<com.konkuk.moru.data.dto.response.RoutineStepResponse>, requiredTime: String = "") {
        Log.d("SharedRoutineViewModel", "🔄 setStepsFromServer 시작: ${steps.size}개 스텝, requiredTime=$requiredTime")
        
        val isSimple = requiredTime.isBlank() // requiredTime이 비어있으면 간편 루틴
        Log.d("SharedRoutineViewModel", "📱 루틴 타입: ${if (isSimple) "간편" else "집중"} (requiredTime=${if (requiredTime.isBlank()) "없음" else requiredTime})")
        
        val stepDataList = steps.map { step ->
            val durationInMinutes = if (step.duration != null) {
                // 스텝에 duration이 있으면 그대로 사용
                convertDurationToMinutes(step.duration)
            } else if (isSimple) {
                // 간편 루틴이면 소요시간 0
                0
            } else {
                // 집중 루틴이고 duration이 null이면 requiredTime을 기반으로 분배
                distributeRequiredTime(requiredTime, steps.size)
            }
            
            RoutineStepData(
                name = step.name,
                duration = durationInMinutes,
                isChecked = true
            )
        }
        
        _selectedSteps.value = stepDataList
        Log.d("SharedRoutineViewModel", "✅ _selectedSteps StateFlow 업데이트 완료")
    }

    // 저장된 스텝 정보를 복원하여 설정
    fun setStepsFromSaved(savedSteps: List<RoutineStepData>) {
        Log.d("SharedRoutineViewModel", "🔄 setStepsFromSaved 시작: ${savedSteps.size}개 스텝")
        
        _selectedSteps.value = savedSteps
        Log.d("SharedRoutineViewModel", "✅ 저장된 스텝 정보 복원 완료")
    }

    // 저장된 선택 상태를 설정 (간편 루틴용)
    fun setSelectedStates(selectedStates: List<Boolean>) {
        Log.d("SharedRoutineViewModel", "🔄 setSelectedStates 시작: ${selectedStates.size}개")
        // 선택 상태는 RoutineSimpleRunScreen에서 직접 사용하므로 별도 저장
        Log.d("SharedRoutineViewModel", "✅ 선택 상태 설정 완료")
    }

    // 루틴 완료 시 모든 상태 초기화
    fun resetRoutineState() {
        Log.d("SharedRoutineViewModel", "🔄 resetRoutineState 호출됨 - 모든 상태 초기화")
        
        _selectedRoutineId.value = null
        _originalRoutineId.value = null
        _routineTitle.value = ""
        _routineCategory.value = ""
        _totalDuration.value = 0
        _routineTags.value = emptyList()
        _isSimple.value = false
        _selectedApps.value = emptyList()
        _selectedSteps.value = emptyList()
        _scheduledTime.value = null
        _scheduledDays.value = emptySet()
        _startNavigation.value = null
        
        Log.d("SharedRoutineViewModel", "✅ SharedRoutineViewModel 상태 초기화 완료")
    }

    // requiredTime을 스텝 개수에 맞게 분배
    private fun distributeRequiredTime(requiredTime: String, stepCount: Int): Int {
        if (requiredTime.isBlank() || stepCount == 0) {
            Log.w("SharedRoutineViewModel", "⚠️ requiredTime이 비어있거나 스텝이 없습니다. 기본값 1분을 사용합니다.")
            return 1
        }
        
        val totalMinutes = convertRequiredTimeToMinutes(requiredTime)
        val averageMinutes = totalMinutes / stepCount
        
        // 최소 1분은 보장
        return maxOf(averageMinutes, 1)
    }

    // ISO 8601 Duration 형식을 분 단위로 변환 (PT30M -> 30분)
    private fun convertRequiredTimeToMinutes(requiredTime: String): Int {
        return try {
            when {
                requiredTime.startsWith("PT") -> {
                    val timePart = requiredTime.substring(2) // "PT" 제거
                    when {
                        timePart.endsWith("H") -> {
                            // 시간 단위 (예: PT1H -> 60분)
                            val hours = timePart.removeSuffix("H").toIntOrNull() ?: 0
                            hours * 60
                        }
                        timePart.endsWith("M") -> {
                            // 분 단위 (예: PT30M -> 30분)
                            timePart.removeSuffix("M").toIntOrNull() ?: 0
                        }
                        timePart.endsWith("S") -> {
                            // 초 단위 (예: PT30S -> 1분)
                            val seconds = timePart.removeSuffix("S").toIntOrNull() ?: 0
                            (seconds + 59) / 60 // 올림 처리
                        }
                        else -> {
                            // 복합 형식 (예: PT1H30M -> 90분)
                            var totalMinutes = 0
                            var currentNumber = ""
                            
                            for (char in timePart) {
                                when (char) {
                                    'H' -> {
                                        totalMinutes += (currentNumber.toIntOrNull() ?: 0) * 60
                                        currentNumber = ""
                                    }
                                    'M' -> {
                                        totalMinutes += currentNumber.toIntOrNull() ?: 0
                                        currentNumber = ""
                                    }
                                    'S' -> {
                                        val seconds = currentNumber.toIntOrNull() ?: 0
                                        totalMinutes += (seconds + 59) / 60
                                        currentNumber = ""
                                    }
                                    else -> currentNumber += char
                                }
                            }
                            totalMinutes
                        }
                    }
                }
                else -> {
                    // 기존 "MM:SS" 형식 지원 (하위 호환성)
                    val parts = requiredTime.split(":")
                    val minutes = parts.getOrNull(0)?.toIntOrNull() ?: 0
                    val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
                    minutes + (seconds / 60)
                }
            }
        } catch (e: Exception) {
            Log.w(
                "SharedRoutineViewModel",
                "⚠️ requiredTime 변환 실패: exception=${e.javaClass.simpleName}, 기본값 1분 사용"
            )
            1
        }
    }

    // ISO 8601 Duration 형식을 분 단위로 변환 (PT15M -> 15분)
    private fun convertDurationToMinutes(duration: String?): Int {
        // duration이 null이면 기본값 1분 반환
        if (duration == null) {
            Log.w("SharedRoutineViewModel", "⚠️ duration이 null입니다. 기본값 1분을 사용합니다.")
            return 1
        }
        
        return try {
            when {
                duration.startsWith("PT") -> {
                    val timePart = duration.substring(2) // "PT" 제거
                    when {
                        timePart.endsWith("H") -> {
                            // 시간 단위 (예: PT1H -> 60분)
                            val hours = timePart.removeSuffix("H").toIntOrNull() ?: 0
                            hours * 60
                        }
                        timePart.endsWith("M") -> {
                            // 분 단위 (예: PT15M -> 15분)
                            timePart.removeSuffix("M").toIntOrNull() ?: 0
                        }
                        timePart.endsWith("S") -> {
                            // 초 단위 (예: PT30S -> 1분)
                            val seconds = timePart.removeSuffix("S").toIntOrNull() ?: 0
                            (seconds + 59) / 60 // 올림 처리
                        }
                        else -> {
                            // 복합 형식 (예: PT1H30M -> 90분)
                            var totalMinutes = 0
                            var currentNumber = ""
                            
                            for (char in timePart) {
                                when (char) {
                                    'H' -> {
                                        totalMinutes += (currentNumber.toIntOrNull() ?: 0) * 60
                                        currentNumber = ""
                                    }
                                    'M' -> {
                                        totalMinutes += currentNumber.toIntOrNull() ?: 0
                                        currentNumber = ""
                                    }
                                    'S' -> {
                                        val seconds = currentNumber.toIntOrNull() ?: 0
                                        totalMinutes += (seconds + 59) / 60
                                        currentNumber = ""
                                    }
                                    else -> currentNumber += char
                                }
                            }
                            totalMinutes
                        }
                    }
                }
                else -> {
                    // 기존 "MM:SS" 형식 지원 (하위 호환성)
                    val parts = duration.split(":")
                    val minutes = parts.getOrNull(0)?.toIntOrNull() ?: 0
                    val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
                    minutes + (seconds / 60)
                }
            }
        } catch (e: Exception) {
            // 변환 실패 시 기본값 반환
            Log.w(
                "SharedRoutineViewModel",
                "⚠️ duration 변환 실패: exception=${e.javaClass.simpleName}, 기본값 1분 사용"
            )
            1
        }
    }


}
