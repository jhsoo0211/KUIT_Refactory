package com.konkuk.moru.presentation.myroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.core.datastore.RoutineSyncBus
import com.konkuk.moru.data.dto.response.MyRoutine.MyRoutineUi
import com.konkuk.moru.data.mapper.toDayOfWeekOrNull
import com.konkuk.moru.data.mapper.toMyLocalTimeOrNull
import com.konkuk.moru.domain.repository.MyRoutineRepository
import com.konkuk.moru.presentation.myroutines.screen.MyRoutinesUiState
import com.konkuk.moru.presentation.myroutines.screen.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import com.konkuk.moru.core.util.toEpochMsOrZero
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart


@HiltViewModel
@OptIn(FlowPreview::class)
class MyRoutinesViewModel @Inject constructor(
    private val repo: MyRoutineRepository
) : ViewModel() {


    private val TAG_REPO = "MyRoutineRepo"

    private val _sourceRoutines = MutableStateFlow<List<MyRoutineUi>>(emptyList())
    val routinesToDisplay: StateFlow<List<MyRoutineUi>> =
        _sourceRoutines.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(MyRoutinesUiState())
    val uiState: StateFlow<MyRoutinesUiState> = _uiState.asStateFlow()

    init {
        // 1) 트리거 스트림을 하나로 합치기
        val uiTriggers = uiState
            .map { it.selectedSortOption to it.selectedDay }
            .distinctUntilChanged()
            .map { Unit }

        val busTriggers = RoutineSyncBus.events
            .filterIsInstance<RoutineSyncBus.Event.MyRoutinesChanged>()
            .map { Unit }

        // 2) 최초 1회 + 디바운스 + 수집은 한 곳에서만
        viewModelScope.launch {
            merge(uiTriggers, busTriggers)
                .onStart { emit(Unit) }           // 최초 1회
                .debounce(150)                     // 짧은 디바운스로 연타 방지
                .collect { loadRoutines() }
        }
    }


    fun onSortOptionSelected(option: SortOption) {
        // ✅ 요일 선택값은 건드리지 않음 (정렬과 독립)
        _uiState.update { it.copy(selectedSortOption = option) }
    }

    fun onDaySelected(day: DayOfWeek?) {
        val newSelectedDay = if (_uiState.value.selectedDay == day) null else day
        _uiState.update { it.copy(selectedDay = newSelectedDay) }
    }

    fun onTrashClick() {
        val currentMode = _uiState.value.isDeleteMode
        _uiState.update { it.copy(isDeleteMode = !currentMode) }
        if (currentMode) uncheckAllRoutines()
    }

    fun onDismissInfoTooltip() {
        _uiState.update { it.copy(showInfoTooltip = false) }
    }

    fun onCheckRoutine(routineId: String, isChecked: Boolean) {
        _sourceRoutines.update { list -> list.map { if (it.routineId == routineId) it.copy(isChecked = isChecked) else it } }
    }

    fun showDeleteDialog() {
        if (routinesToDisplay.value.any { it.isChecked }) _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun dismissDeleteSuccessDialog() {
        _uiState.update { it.copy(showDeleteSuccessDialog = false) }
    }

    fun deleteCheckedRoutines() {
        viewModelScope.launch {
            val targets = _sourceRoutines.value.filter { it.isChecked }.map { it.routineId }

            val succeed = mutableListOf<String>()
            val failed = mutableListOf<String>()

            for (id in targets) {
                val ok = repo.deleteRoutineSafe(id)
                if (ok) succeed += id else failed += id
            }

            // 성공한 것만 즉시 제거(낙관적)
            if (succeed.isNotEmpty()) {
                _sourceRoutines.update { list -> list.filterNot { it.routineId in succeed } }
            }

            // UI 피드백
            _uiState.update {
                it.copy(
                    isDeleteMode = false,
                    showDeleteDialog = false,
                    showDeleteSuccessDialog = failed.isEmpty()
                )
            }

            // 실패 안내 토스트/스낵바가 필요하면 여기에 호출
            if (failed.isNotEmpty()) {
                // e.g. sendEvent(Snackbar("일부 루틴을 삭제하지 못했습니다. 잠시 후 다시 시도해 주세요."))
            }

            // 최신 서버 상태 재조회
            refreshRoutines()
        }
    }


    fun onShowInfoTooltip() {
        _uiState.update { it.copy(showInfoTooltip = true) }
    }

    fun onLikeClick(routineId: String) {
        _sourceRoutines.update { list ->
            list.map { r ->
                if (r.routineId == routineId)
                    r.copy(
                        isLiked = !r.isLiked,
                        likes = if (r.isLiked) r.likes - 1 else r.likes + 1
                    )
                else r
            }
        }
    }

    private fun uncheckAllRoutines() {
        _sourceRoutines.update { list -> list.map { it.copy(isChecked = false) } }
    }

    /** ✅ 요구사항 반영:
     *  - dayParam = 사용자가 탭에서 고른 요일(없으면 null)
     *  - 항상 sortType과 함께 서버로 전달 → 서버가 정렬 + (선택시)요일 필터를 함께 적용
     *  - 서버가 TIME+null을 아직 막아 에러면, 오늘 요일로 1회 폴백
     */
    private suspend fun fetchFromServer() {
        val state = _uiState.value
        val sortType = when (state.selectedSortOption) {
            SortOption.BY_TIME -> "TIME"
            SortOption.LATEST -> "LATEST"
            SortOption.POPULAR -> "POPULAR"
        }
        val selectedDay = state.selectedDay


        val base: List<MyRoutineUi> =
            if (selectedDay == null) {
                // ✅ 요일 미선택: 서버에 dayOfWeek=null로 그대로 전달 → "전체 목록"
                repo.getMyRoutines(sortType, null, 0, 50)
            } else {
                // ✅ 요일 선택: 서버가 TIME+day로 필터링 (sortType 무시 X)
                //   (서버가 sortType도 적용한다면 그대로, 아니면 아래서 보정정렬)
                repo.getMyRoutines(sortType, selectedDay, 0, 50)
            }

        // (선택) 서버 정렬이 들쭉날쭉할 때만 보정 정렬
        val result = when (sortType) {
            "POPULAR" -> base.sortedByDescending { it.likes }
            "LATEST" -> base.sortedByDescending { it.createdAt.toEpochMsOrZero() }
            else -> base // TIME은 서버 정렬 신뢰(필요하면 scheduledTime로 로컬정렬)
        }

        _sourceRoutines.value = result
    }

    /*private suspend fun fetchFromServer() {
        val state = _uiState.value
        val sortType = when (state.selectedSortOption) {
            SortOption.BY_TIME -> "TIME"
            SortOption.LATEST -> "LATEST"
            SortOption.POPULAR -> "POPULAR"
        }
        val selectedDay = state.selectedDay


        val result: List<MyRoutineUi> = try {
            if (selectedDay == null) {
                if (sortType == "TIME") {
                    // ✅ 서버 TIME 정렬을 '오늘 요일'로 명시적으로 사용
                    val today = LocalDate.now(ZoneId.systemDefault()).dayOfWeek
                    val res = repo.getMyRoutines("TIME", today, 0, 50)
                    // 서버가 이미 시간순 정렬을 보장한다면 그대로 반환
                    res
                } else {
                    val res = repo.getMyRoutines(sortType, null, 0, 50)
                    res
                }
            } else {
                val dayFiltered = repo.getMyRoutines("TIME", selectedDay, 0, 50)
                when (sortType) {
                    "POPULAR" -> dayFiltered.sortedByDescending { it.likes }
                    "LATEST" -> dayFiltered.sortedByDescending { it.createdAt.toEpochMsOrZero() }
                    else -> dayFiltered.sortedBy { it.scheduledTime ?: LocalTime.MAX }
                }
            }
        } catch (e: Exception) {
            if (sortType == "TIME" && selectedDay == null) {
                val today = LocalDate.now(ZoneId.systemDefault()).dayOfWeek
                repo.getMyRoutines("TIME", today, 0, 50)
            } else throw e
        }

        _sourceRoutines.value = result
    }*/

    fun loadRoutines() {
        viewModelScope.launch { fetchFromServer() }
    }

    fun refreshRoutines() = loadRoutines()


    private val HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss")


    //스케줄 관련 로직
    private var openPickerJob: kotlinx.coroutines.Job? = null

    fun openTimePicker(routineId: String) {
        openPickerJob?.cancel()

// ✅ 이전 루틴에서 남은 편집 상태가 있으면 먼저 초기화 (PATCH/POST 오동작 방지)
        _uiState.update {
            it.copy(
                editingRoutineId = routineId,
                editingScheduleId = null,
                initialTimeForSheet = null,
                initialDaysForSheet = emptySet(),
                initialAlarmForSheet = true
            )
        }


        openPickerJob = viewModelScope.launch {
            val schedules = repo.getSchedules(routineId)

            val order = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
            val sorted = schedules.sortedBy { order.indexOf(it.dayOfWeek) }

            val rawDays = sorted.map { it.dayOfWeek }
            val parsedDays = sorted.mapNotNull { it.dayOfWeek.toDayOfWeekOrNull() }.toSet()

            val timeSet = sorted.mapNotNull { it.time.toMyLocalTimeOrNull() }.toSet()
            val initTime = if (timeSet.size == 1) timeSet.first() else null
            val initDays = parsedDays
            val initAlarm = sorted.any { it.alarmEnabled }
            val firstSchId = sorted.firstOrNull()?.id

            _uiState.update {
                it.copy(
                    editingRoutineId = routineId,
                    editingScheduleId = firstSchId,
                    initialTimeForSheet = initTime,
                    initialDaysForSheet = initDays,
                    initialAlarmForSheet = initAlarm
                )
            }
        }
    }

    fun closeTimePicker() {
        _uiState.update {
            it.copy(
                editingRoutineId = null,
                editingScheduleId = null,
                initialTimeForSheet = null,
                initialDaysForSheet = emptySet(),
                initialAlarmForSheet = true
            )
        }
    }

    fun onConfirmTimeSet(routineId: String, time: LocalTime, days: Set<DayOfWeek>, alarm: Boolean) {


        viewModelScope.launch {


            try {
                if (days.isEmpty()) return@launch

                val schId = _uiState.value.editingScheduleId
                if (schId != null) {
                    repo.updateSchedule(routineId, schId, time.format(HH_MM_SS), days, alarm)
                } else {
                    repo.createSchedule(routineId, time.format(HH_MM_SS), days, alarm)
                }

                // 💡 현재 선택된 요일이 새 요일셋에 없으면 필터 해제
                val curSelected = _uiState.value.selectedDay
                if (curSelected != null && !days.contains(curSelected)) {
                    _uiState.update { it.copy(selectedDay = null) }
                }

                RoutineSyncBus.publish(RoutineSyncBus.Event.MyRoutinesChanged)
                // ✅ 즉시 반영 (버스 지연/디바운스 상황 대비)
                loadRoutines()
            } catch (_: Exception) {
                refreshRoutines()
            } finally {
                closeTimePicker()
            }
        }
    }

}
