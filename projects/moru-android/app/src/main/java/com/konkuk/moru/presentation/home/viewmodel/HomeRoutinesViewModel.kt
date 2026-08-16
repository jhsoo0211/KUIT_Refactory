package com.konkuk.moru.presentation.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.core.datastore.RoutineSyncBus
import com.konkuk.moru.core.datastore.SchedulePreference
import com.konkuk.moru.data.dto.response.HomeScheduleResponse
import com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponseV1
import com.konkuk.moru.data.mapper.toDomain
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.data.repositoryimpl.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeRoutinesViewModel @Inject constructor(
    private val repo: RoutineRepository,
    private val myRoutineRepo: com.konkuk.moru.domain.repository.MyRoutineRepository
) : ViewModel() {

    private companion object {
        private const val TAG = "HomeRoutinesVM"
    }

    init {
        // RoutineSyncBus 구독하여 내 루틴 변경 시 자동 리프레시
        viewModelScope.launch {
            RoutineSyncBus.events
                .filterIsInstance<RoutineSyncBus.Event.MyRoutinesChanged>()
                .collectLatest {
                    Log.d(TAG, "🔄 RoutineSyncBus.MyRoutinesChanged 이벤트 수신 - 내 루틴 리프레시")
                    loadMyRoutines()
                    loadTodayRoutines()
                }
        }
    }

    private val _serverRoutines = MutableStateFlow<List<Routine>>(emptyList())
    val serverRoutines: StateFlow<List<Routine>> = _serverRoutines

    // 내 루틴 전체(하단 카드)
    private val _myRoutines = MutableStateFlow<List<Routine>>(emptyList())
    val myRoutines: StateFlow<List<Routine>> = _myRoutines

    // 스케줄 정보가 병합된 루틴 목록
    private val _scheduledRoutines = MutableStateFlow<List<Routine>>(emptyList())
    val scheduledRoutines: StateFlow<List<Routine>> = _scheduledRoutines

    // 루틴 상세 정보 (스텝 포함)
    private val _routineDetail = MutableStateFlow<RoutineDetailResponseV1?>(null)
    val routineDetail: StateFlow<RoutineDetailResponseV1?> = _routineDetail

    fun loadTodayRoutines(page: Int = 0, size: Int = 20) {
        Log.d(TAG, "🔄 loadTodayRoutines 호출됨: page=$page, size=$size")
        Log.d(TAG, "🌐 네트워크 연결 테스트 시작...")

        viewModelScope.launch {
            Log.d(TAG, "🚀 코루틴 시작됨!")
            Log.d(TAG, "🔄 loadTodayRoutines 코루틴 시작")
            try {
                Log.d(TAG, "🔗 서버 연결 시도 중...")
                Log.d(TAG, "🌐 API 엔드포인트: /api/routines/today (page=$page, size=$size)")

                val pageRes = repo.getMyRoutinesToday(page, size)
                Log.d(TAG, "✅ loadTodayRoutines 성공!")
                Log.d(TAG, "📊 서버 응답: total=${pageRes.totalElements}, page=${pageRes.number}, size=${pageRes.size}, contentSize=${pageRes.content.size}")

                _serverRoutines.value = pageRes.content.map { it.toDomain() }
                Log.d(TAG, "✅ _serverRoutines StateFlow 업데이트 완료: ${_serverRoutines.value.size}개")
            } catch (e: Exception) {
                Log.e(TAG, "loadTodayRoutines failed: ${e.javaClass.simpleName}")

                when (e) {
                    is retrofit2.HttpException -> {
                        val code = e.code()
                        Log.e(TAG, "HTTP $code")

                        if (code == 500) {
                            Log.e(TAG, "🚨 서버 내부 오류 (500) - 서버 점검 중일 수 있습니다")
                        } else if (code == 404) {
                            Log.e(TAG, "🚨 서버 엔드포인트를 찾을 수 없습니다 (404)")
                        } else if (code == 403) {
                            Log.e(TAG, "🚨 접근 권한이 없습니다 (403)")
                        }
                    }
                    is java.net.SocketTimeoutException -> {
                        Log.e(TAG, "🚨 네트워크 타임아웃 발생")
                    }
                    is java.net.UnknownHostException -> {
                        Log.e(TAG, "🚨 서버 호스트를 찾을 수 없습니다")
                    }
                    is javax.net.ssl.SSLHandshakeException -> {
                        Log.e(TAG, "🚨 SSL 인증서 문제 발생")
                    }
                    is java.net.ConnectException -> {
                        Log.e(TAG, "🚨 서버 연결 실패")
                    }
                    else -> {
                        Log.e(TAG, "🚨 기타 네트워크 오류: ${e.javaClass.simpleName}")
                    }
                    }

                    // 서버 오류 시 빈 리스트로 설정 (UI가 깨지지 않도록)
                    _serverRoutines.value = emptyList()
                    Log.d(TAG, "💡 서버 오류로 인해 빈 리스트로 설정됨. 서버 상태를 확인해주세요.")
                }
        }
    }

    // 전체 루틴 로드
    fun loadMyRoutines(page: Int = 0, size: Int = 100) = viewModelScope.launch {
        Log.d(TAG, "🔄 loadMyRoutines 호출됨: page=$page, size=$size")
        runCatching { repo.getAllMyRoutines(page, size) }
            .onSuccess { pageRes ->
                Log.d(TAG, "✅ loadMyRoutines 성공!")
                Log.d(TAG, "📊 응답 데이터: total=${pageRes.totalElements}, page=${pageRes.number}, size=${pageRes.size}, contentSize=${pageRes.content.size}")
                
                val routines = pageRes.content.map { it.toDomain() }
                Log.d(TAG, "🔄 도메인 변환 완료: ${routines.size}개")
                
                // 첫 번째 로드인지 확인 (현재 루틴 목록이 비어있는 경우)
                val isFirstLoad = _myRoutines.value.isEmpty()
                
                if (isFirstLoad) {
                    // 첫 번째 로드: 기본 정렬 적용
                    val sortedRoutines = routines.sortedWith(
                        compareByDescending<Routine> { it.scheduledTime == null }
                            .thenBy { it.scheduledTime ?: java.time.LocalTime.MAX }
                    )
                    
                    Log.d(TAG, "🔄 첫 번째 로드: 기본 정렬 적용 - 시간 미설정 ${sortedRoutines.count { it.scheduledTime == null }}개, 시간 설정 ${sortedRoutines.count { it.scheduledTime != null }}개")
                    _myRoutines.value = sortedRoutines
                } else {
                    // 이후 로드: 진행 중인 루틴들의 정렬 유지
                    val currentRoutines = _myRoutines.value
                    val runningRoutines = currentRoutines.filter { it.isRunning }
                    val nonRunningRoutines = routines.filter { !it.isRunning }
                    
                    // 진행 중이지 않은 루틴들을 기본 정렬 기준으로 정렬
                    val sortedNonRunning = nonRunningRoutines.sortedWith(
                        compareByDescending<Routine> { it.scheduledTime == null }
                            .thenBy { it.scheduledTime ?: java.time.LocalTime.MAX }
                    )
                    
                    // 진행 중인 루틴들 + 정렬된 나머지 루틴들
                    val finalRoutines = runningRoutines + sortedNonRunning
                    
                    Log.d(TAG, "🔄 이후 로드: 진행중 루틴 정렬 유지 - 진행중 ${runningRoutines.size}개, 시간 미설정 ${sortedNonRunning.count { it.scheduledTime == null }}개, 시간 설정 ${sortedNonRunning.count { it.scheduledTime != null }}개")
                    _myRoutines.value = finalRoutines
                }
                Log.d(TAG, "✅ _myRoutines StateFlow 업데이트 완료")
            }
            .onFailure { e ->
                Log.e(TAG, "loadMyRoutines failed: ${e.javaClass.simpleName}")
                if (e is retrofit2.HttpException) {
                    Log.e(TAG, "HTTP ${e.code()}")
                }
                _myRoutines.value = emptyList()
            }
    }

    // 루틴 상세 정보 로드 (스텝 포함)
    fun loadRoutineDetail(routineId: String) = viewModelScope.launch {
        runCatching { repo.getRoutineDetail(routineId) }
            .onSuccess { detail ->
                Log.d(TAG, "✅ loadRoutineDetail 성공!")
                Log.d(TAG, "   - 스텝 개수: ${detail.steps.size}")
                _routineDetail.value = detail
                Log.d(TAG, "✅ _routineDetail StateFlow 업데이트 완료")

                // 스텝 정보를 SharedRoutineViewModel에 직접 설정
                Log.d(TAG, "🔄 스텝 정보를 SharedRoutineViewModel에 설정")
                setStepsToSharedViewModel(detail.steps)
            }
            .onFailure { e ->
                Log.e(TAG, "loadRoutineDetail failed: ${e.javaClass.simpleName}")
                if (e is retrofit2.HttpException) {
                    Log.e(TAG, "HTTP ${e.code()}")
                }

                _routineDetail.value = null
            }
    }

    // MyRoutineDetailDto를 사용하여 루틴 상세 정보 로드 (사용앱 정보 포함)
    fun loadMyRoutineDetail(routineId: String) = viewModelScope.launch {
        runCatching {
            // MyRoutineRepository를 사용하여 사용앱 정보가 포함된 상세 정보 가져오기
            myRoutineRepo.getRoutineDetailRaw(routineId)
        }
        .onSuccess { detail ->
            Log.d(TAG, "✅ loadMyRoutineDetail 성공: steps=${detail.steps.size}, apps=${detail.apps.size}")
            
            // 스텝 정보를 SharedRoutineViewModel에 설정
            Log.d(TAG, "🔄 스텝 정보를 SharedRoutineViewModel에 설정")
            val stepDataList = detail.steps.map { step ->
                com.konkuk.moru.presentation.home.RoutineStepData(
                    name = step.name,
                    duration = step.estimatedTime?.let { time ->
                        // ISO 8601 Duration 형식을 분 단위로 변환
                        when {
                            time.startsWith("PT") -> {
                                val timePart = time.substring(2)
                                when {
                                    timePart.endsWith("H") -> {
                                        val hours = timePart.removeSuffix("H").toIntOrNull() ?: 0
                                        hours * 60
                                    }
                                    timePart.endsWith("M") -> {
                                        timePart.removeSuffix("M").toIntOrNull() ?: 0
                                    }
                                    timePart.endsWith("S") -> {
                                        val seconds = timePart.removeSuffix("S").toIntOrNull() ?: 0
                                        (seconds + 59) / 60 // 올림 처리
                                    }
                                    else -> 1
                                }
                            }
                            else -> 1
                        }
                    } ?: 1,
                    isChecked = true
                )
            }
            
            _sharedViewModel?.let { shared ->
                shared.setSelectedSteps(stepDataList)
                Log.d(TAG, "✅ SharedRoutineViewModel에 스텝 정보 설정 완료: ${stepDataList.size}개")
            }
            
            // 사용앱 정보를 SharedRoutineViewModel에 설정
            Log.d(TAG, "🔄 사용앱 정보를 SharedRoutineViewModel에 설정")
            setAppsToSharedViewModel(detail.apps)
            
            // 이미지 URL을 SharedRoutineViewModel에 설정
            _sharedViewModel?.setRoutineImageUrl(detail.imageUrl)
            
            // 기존 RoutineDetailResponseV1 형식으로 변환하여 _routineDetail에 설정
            // (기존 코드와의 호환성을 위해)
                         val convertedDetail = com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponseV1(
                 id = detail.id,
                title = detail.title,
                description = detail.description,
                category = if (detail.isSimple) "간편" else "집중",
                tags = detail.tags,
                                 steps = detail.steps.map { step ->
                     com.konkuk.moru.data.dto.response.RoutineStepResponse(
                         id = step.id,
                         order = step.stepOrder,
                         name = step.name,
                         duration = step.estimatedTime,
                         description = null
                     )
                 },
                author = com.konkuk.moru.data.dto.response.Routine.AuthorResponse(
                    id = detail.author.id,
                    name = detail.author.nickname,
                    profileImageUrl = detail.author.profileImageUrl
                ),
                authorName = detail.author.nickname
            )
            
            _routineDetail.value = convertedDetail
            Log.d(TAG, "✅ _routineDetail StateFlow 업데이트 완료")
        }
        .onFailure { e ->
            Log.e(TAG, "loadMyRoutineDetail failed: ${e.javaClass.simpleName}")
            _routineDetail.value = null
        }
    }

    // SharedRoutineViewModel 참조
    private var _sharedViewModel: com.konkuk.moru.presentation.routinefocus.viewmodel.SharedRoutineViewModel? = null

    // 스텝 정보를 SharedRoutineViewModel에 설정
    private fun setStepsToSharedViewModel(steps: List<com.konkuk.moru.data.dto.response.RoutineStepResponse>) {
        Log.d(TAG, "🔄 setStepsToSharedViewModel 호출: ${steps.size}개 스텝")
        
        _sharedViewModel?.let { shared ->
            // 스텝 정보를 RoutineStepData로 변환하여 설정
            val stepDataList = steps.map { step ->
                com.konkuk.moru.presentation.home.RoutineStepData(
                    name = step.name,
                    duration = step.duration?.let { duration ->
                        // ISO 8601 Duration 형식을 분 단위로 변환
                        when {
                            duration.startsWith("PT") -> {
                                val timePart = duration.substring(2)
                                when {
                                    timePart.endsWith("H") -> {
                                        val hours = timePart.removeSuffix("H").toIntOrNull() ?: 0
                                        hours * 60
                                    }
                                    timePart.endsWith("M") -> {
                                        timePart.removeSuffix("M").toIntOrNull() ?: 0
                                    }
                                    timePart.endsWith("S") -> {
                                        val seconds = timePart.removeSuffix("S").toIntOrNull() ?: 0
                                        (seconds + 59) / 60 // 올림 처리
                                    }
                                    else -> 1
                                }
                            }
                            else -> 1
                        }
                    } ?: 1,
                    isChecked = true
                )
            }
            
            shared.setSelectedSteps(stepDataList)
            Log.d(TAG, "✅ SharedRoutineViewModel에 스텝 정보 설정 완료: ${stepDataList.size}개")
        } ?: run {
            Log.w(TAG, "⚠️ SharedRoutineViewModel이 설정되지 않음")
        }
    }

    // 사용앱 정보를 SharedRoutineViewModel에 설정
    private fun setAppsToSharedViewModel(apps: List<com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineDetailDto.AppDto>) {
        Log.d(TAG, "🔄 setAppsToSharedViewModel 호출: ${apps.size}개 앱")
        
        _sharedViewModel?.let { shared ->
            // AppDto를 AppDto로 변환하여 설정 (패키지명과 이름만 있음)
            val appDtoList = apps.map { app ->
                com.konkuk.moru.presentation.routinefeed.data.AppDto(
                    name = app.name,
                    packageName = app.packageName
                )
            }
            
            shared.setSelectedApps(appDtoList)
            Log.d(TAG, "✅ SharedRoutineViewModel에 사용앱 정보 설정 완료: ${appDtoList.size}개")
        } ?: run {
            Log.w(TAG, "⚠️ SharedRoutineViewModel이 설정되지 않음")
        }
    }

    // SharedRoutineViewModel 인스턴스를 받아서 설정
    fun setSharedRoutineViewModel(sharedViewModel: com.konkuk.moru.presentation.routinefocus.viewmodel.SharedRoutineViewModel) {
        _sharedViewModel = sharedViewModel
        Log.d(TAG, "✅ SharedRoutineViewModel 설정 완료")
    }
    
    // 로컬 스케줄 정보 가져오기
    suspend fun getLocalSchedule(context: Context, routineId: String): com.konkuk.moru.core.datastore.SchedulePreference.ScheduleInfo? {
        return try {
            val localSchedules = com.konkuk.moru.core.datastore.SchedulePreference.getSchedules(context)
            localSchedules.find { it.routineId == routineId }
        } catch (e: Exception) {
            Log.e(TAG, "getLocalSchedule failed: ${e.javaClass.simpleName}")
            null
        }
    }

    // myRoutines 업데이트 (진행중인 루틴을 맨 앞으로 이동할 때 사용)
    fun updateMyRoutines(updatedRoutines: List<Routine>) {
        Log.d(TAG, "🔄 updateMyRoutines 호출: ${updatedRoutines.size}개 루틴")
        _myRoutines.value = updatedRoutines
        Log.d(TAG, "✅ _myRoutines StateFlow 업데이트 완료")
    }

    // 서버에서 스케줄 정보 가져오기
    suspend fun getRoutineSchedules(routineId: String): List<HomeScheduleResponse> {
        return try {
            val schedules = repo.getRoutineSchedules(routineId)
            Log.d(TAG, "✅ 스케줄 정보 가져오기 성공: ${schedules.size}개")

            if (schedules.isEmpty()) {
                Log.w(TAG, "⚠️ 스케줄이 비어있음 - 서버에 스케줄 데이터가 없을 수 있음")
            }
            schedules
        } catch (e: Exception) {
            Log.e(TAG, "getRoutineSchedules failed: ${e.javaClass.simpleName}")
            if (e is retrofit2.HttpException) {
                Log.e(TAG, "HTTP ${e.code()}")
            }
            emptyList()
        }
    }

    // 로컬 스케줄 정보와 병합
    fun mergeWithLocalSchedule(context: Context) = viewModelScope.launch {
        try {
            val localSchedules = SchedulePreference.getSchedules(context)
            Log.d(TAG, "로컬 스케줄 정보 로드: ${localSchedules.size}개")

            // 서버 루틴과 로컬 스케줄 정보 병합
            val mergedRoutines = _serverRoutines.value.map { routine ->
                val localSchedule = localSchedules.find { it.routineId == routine.routineId }
                if (localSchedule != null) {
                    routine.copy(
                        scheduledDays = SchedulePreference.stringsToDayOfWeeks(localSchedule.scheduledDays),
                        scheduledTime = SchedulePreference.stringToLocalTime(localSchedule.scheduledTime)
                    )
                } else {
                    routine
                }
            }

            _scheduledRoutines.value = mergedRoutines
            Log.d(TAG, "스케줄 정보 병합 완료: ${mergedRoutines.size}개")

        } catch (e: Exception) {
            Log.e(TAG, "mergeWithLocalSchedule failed: ${e.javaClass.simpleName}")
            _scheduledRoutines.value = _serverRoutines.value
        }
    }
}
