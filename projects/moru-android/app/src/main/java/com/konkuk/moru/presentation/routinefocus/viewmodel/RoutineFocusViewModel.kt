package com.konkuk.moru.presentation.routinefocus.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.presentation.routinefeed.data.AppDto
import com.konkuk.moru.presentation.routinefocus.screen.parseTimeToSeconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RoutineFocusViewModel : ViewModel() {

    // ViewModel에 추가해야 할 메서드들
    fun getMemoText(stepNumber: Int): String {
        return _stepMemos.value[stepNumber] ?: ""
    }


    // 시간이 흐르고 있는지 유무
    var isTimerRunning by mutableStateOf(false)
        private set

    // 시간 초과 상태인지
    var isTimeout by mutableStateOf(false)
        private set

    // 현재 진행 중인 스탭이 어디인지
    var currentStep by mutableIntStateOf(1)
        private set

    // 현재 스텝 설정 함수
    fun updateCurrentStep(step: Int) {
        currentStep = step
    }

    // 각 스탭의 현재 경과 시간
    var elapsedSeconds by mutableIntStateOf(0)
        private set

    // 총 경과 시간
    var totalElapsedSeconds by mutableIntStateOf(0)
        private set

    // 가로 세로 모드를 판단
    var isLandscapeMode by mutableStateOf(false)
        private set

    fun updateMemoText(stepNumber: Int, text: String) {
        _stepMemos.value = _stepMemos.value.toMutableMap().apply {
            put(stepNumber, text)
        }
    }

    // 가로 모드 토글 함수
    fun toggleLandscapeMode() {
        Log.d("RoutineFocusViewModel", "🔄 가로모드 토글: ${if (isLandscapeMode) "가로" else "세로"} → ${if (!isLandscapeMode) "가로" else "세로"}")
        Log.d("RoutineFocusViewModel", "📱 토글 전 팝업 상태 - showAppIcons: $showAppIcons, showMemoPad: $showMemoPad")
        Log.d("RoutineFocusViewModel", "📝 토글 전 메모 개수: ${_stepMemos.value.size}")
        
        isLandscapeMode = !isLandscapeMode
        
        Log.d("RoutineFocusViewModel", "📱 토글 후 팝업 상태 - showAppIcons: $showAppIcons, showMemoPad: $showMemoPad")
        Log.d("RoutineFocusViewModel", "📝 토글 후 메모 개수: ${_stepMemos.value.size}")
    }

    fun setLandscapeModeOn() {
        isLandscapeMode = true
    }

    fun setLandscapeModeOff() {
        isLandscapeMode = false
    }

    // 다크 모드 판단
    var isDarkMode by mutableStateOf(false)
        private set

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    fun setDarkModeOn() {
        isDarkMode = true
    }

    fun setDarkModeOff() {
        isDarkMode = false
    }

    // 정지/재생 버튼 상태 저장
    private var stepLimit = 0

    fun setStepLimitFromTimeString(limitInSeconds: Int) {
        stepLimit = limitInSeconds
    }



    var isUserPaused by mutableStateOf(false)
        private set

    fun togglePause() {
        isUserPaused = !isUserPaused
        if (isUserPaused) {
            pauseTimer()
        } else {
            startTimer()
        }
    }



    // 설정 팝업 상태 저장
    var isSettingsPopupVisible by mutableStateOf(false)
        private set



    fun toggleSettingsPopup() {
        isSettingsPopupVisible = !isSettingsPopupVisible
    }

    fun closeSettingsPopup() {
        isSettingsPopupVisible = false
    }

    // 화면 차단 오버레이 관련
    var isScreenBlockOverlayVisible by mutableStateOf(false)
    private val _selectedApps = mutableStateOf<List<AppDto>>(emptyList())
    val selectedApps: List<AppDto>
        get() = _selectedApps.value
    
    fun showScreenBlockOverlay(apps: List<AppDto>) {
        Log.d("RoutineFocusViewModel", "🛡️ showScreenBlockOverlay 호출: apps.size=${apps.size}")
        _selectedApps.value = apps
        isScreenBlockOverlayVisible = true
        Log.d("RoutineFocusViewModel", "🛡️ isScreenBlockOverlayVisible = $isScreenBlockOverlayVisible")
    }
    
    fun hideScreenBlockOverlay() {
        Log.d("RoutineFocusViewModel", "🛡️ hideScreenBlockOverlay 호출")
        isScreenBlockOverlayVisible = false
        Log.d("RoutineFocusViewModel", "🛡️ isScreenBlockOverlayVisible = $isScreenBlockOverlayVisible")
    }
    
    fun setSelectedApps(apps: List<AppDto>) {
        Log.d("RoutineFocusViewModel", "🔄 setSelectedApps 호출됨")
        Log.d("RoutineFocusViewModel", "📱 전달받은 앱 개수: ${apps.size}")
        _selectedApps.value = apps
        Log.d("RoutineFocusViewModel", "✅ selectedApps 설정 완료: ${_selectedApps.value.size}개")
    }
    
    // 기존 팝업 관련 (하위 호환성 유지)
    var isScreenBlockPopupVisible by mutableStateOf(false)
    
    fun showScreenBlockPopup(apps: List<AppDto>) {
        _selectedApps.value = apps
        isScreenBlockPopupVisible = true
    }
    
    fun hideScreenBlockPopup() {
        isScreenBlockPopupVisible = false
    }
    
    // 온보딩 팝업창 관련
    var isOnboardingPopupVisible by mutableStateOf(false)
    
    fun showOnboardingPopup() {
        isOnboardingPopupVisible = true
    }
    
    fun hideOnboardingPopup() {
        isOnboardingPopupVisible = false
    }



    // 집중 루틴 활성화 상태
    var _isFocusRoutineActive by mutableStateOf(false)
        private set

    // 허용된 앱 실행 플래그
    var _isPermittedAppLaunch by mutableStateOf(false)
        private set

    fun startFocusRoutine() {
        android.util.Log.d("RoutineFocusViewModel", "🚀 startFocusRoutine 호출됨!")
        _isFocusRoutineActive = true
        android.util.Log.d("RoutineFocusViewModel", "✅ _isFocusRoutineActive = $_isFocusRoutineActive")
    }

    fun endFocusRoutine() {
        _isFocusRoutineActive = false
        _isPermittedAppLaunch = false
        
        // 타이머 Job 취소
        timerJob?.cancel()
        timerJob = null
        
        // 루틴 종료 시 모든 상태 초기화
        isTimerRunning = false
        isTimeout = false
        currentStep = 1
        elapsedSeconds = 0
        totalElapsedSeconds = 0
        isUserPaused = false
        stepLimit = 0
        
        // 팝업 상태들도 초기화
        isAppIconsVisible = false
        showMemoPad = false
        isScreenBlockOverlayVisible = false
        isScreenBlockPopupVisible = false
        isOnboardingPopupVisible = false
        isSettingsPopupVisible = false
        
        // 선택된 앱들 초기화
        _selectedApps.value = emptyList()
        
        // 스텝별 메모 초기화
        _stepMemos.value = emptyMap()
        
        // 화면 모드 초기화 (세로 모드로)
        isLandscapeMode = false
        
        // 다크 모드 초기화 (기본값으로)
        isDarkMode = false
        
        Log.d("RoutineFocusViewModel", "🔄 루틴 종료: 모든 상태 초기화 완료")
    }

    fun setPermittedAppLaunch(permitted: Boolean) {
        _isPermittedAppLaunch = permitted
    }

    // 외부에서 읽기 전용으로 접근할 수 있는 프로퍼티
    val isPermittedAppLaunch: Boolean
        get() = _isPermittedAppLaunch

    val isFocusRoutineActive: Boolean
        get() = _isFocusRoutineActive

    // 사용 앱 팝업 상태 저장
    var isAppIconsVisible by mutableStateOf(false)
        private set

    // 세로모드와의 호환성을 위한 별칭
    val showAppIcons: Boolean
        get() = isAppIconsVisible

    fun toggleAppIcons() {
        android.util.Log.d("RoutineFocusViewModel", "📱 앱 아이콘 팝업 토글: $isAppIconsVisible → ${!isAppIconsVisible}")
        isAppIconsVisible = !isAppIconsVisible
        android.util.Log.d("RoutineFocusViewModel", "✅ isAppIconsVisible = $isAppIconsVisible")
    }

    fun hideAppIcons() {
        isAppIconsVisible = false
    }

    // 메모장 팝업 상태 저장
    var showMemoPad by mutableStateOf(false)
        private set

    // 스텝별 메모 저장
    private val _stepMemos = MutableStateFlow<Map<Int, String>>(emptyMap())
    val stepMemos: StateFlow<Map<Int, String>> = _stepMemos.asStateFlow()

    fun toggleMemoPad() {
        Log.d("RoutineFocusViewModel", "📝 메모장 팝업 토글: $showMemoPad → ${!showMemoPad}")
        showMemoPad = !showMemoPad
    }

    fun hideMemoPad() {
        showMemoPad = false
    }

    // 특정 스텝의 메모 저장
    fun saveStepMemo(step: Int, memo: String) {
        Log.d("RoutineFocusViewModel", "📝 메모 저장 요청: memoLength=${memo.length}, memoCount=${_stepMemos.value.size}")
        
        _stepMemos.value = _stepMemos.value.toMutableMap().apply {
            put(step, memo)
        }
        
        Log.d("RoutineFocusViewModel", "📝 메모 저장 완료: memoCount=${_stepMemos.value.size}")
    }

    // 특정 스텝의 메모 가져오기
    fun getStepMemo(step: Int): String {
        val memo = _stepMemos.value[step] ?: ""
        Log.d("RoutineFocusViewModel", "📖 메모 불러오기 완료: hasMemo=${memo.isNotEmpty()}")
        return memo
    }
    
    // 특정 스텝의 메모를 StateFlow로 제공
    fun getStepMemoFlow(step: Int): StateFlow<String> {
        return MutableStateFlow(_stepMemos.value[step] ?: "").asStateFlow()
    }
    
    // 타이머 Job 관리
    private var timerJob: Job? = null

    // 타이머 시작 함수
    fun startTimer() {
        if (isTimerRunning) return
        
        // 기존 타이머 Job이 있다면 취소
        timerJob?.cancel()
        
        isTimerRunning = true
        isTimeout = false
        timerJob = viewModelScope.launch {
            while (isTimerRunning) {
                delay(1000)
                elapsedSeconds++
                if (!isTimeout && elapsedSeconds > stepLimit) {
                    isTimeout = true
                }
            }
        }
    }


    // 타이머 일시정지 함수
    fun pauseTimer() {
        isTimerRunning = false
        timerJob?.cancel()
        timerJob = null
    }

    // 타이머 재개 함수
    fun resumeTimer() {
        if (!isTimerRunning) {
            isUserPaused = false
            startTimer()
        }
    }





    // 다음 스텝으로 넘어갈 때 호출하는 함수
    fun nextStep(newTimeString: String) {
        totalElapsedSeconds += elapsedSeconds
        elapsedSeconds = 0
        isTimeout = false
        currentStep++
        setStepLimitFromTimeString(parseTimeToSeconds(newTimeString))
    }

    // 현재 스텝의 시간을 리셋할 때 호출하는 함수
    fun resetTimer() {
        elapsedSeconds = 0
        isTimeout = false
    }
}
