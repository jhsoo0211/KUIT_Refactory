package com.konkuk.moru.presentation.myactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.domain.model.MyActRecord
import com.konkuk.moru.domain.model.MyActRecordCursor
import com.konkuk.moru.domain.repository.MyActRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import android.util.Log

data class MyActRecordUi(
    val id: String,
    val title: String,
    val tags: List<String>,
    val isComplete: Boolean,
    val startedAt: LocalDate,
    val durationSec: Long = 0L,
    val stepNames: List<String> = emptyList(),
    val stepDurations: List<String> = emptyList(),
    val category: String = "",
    val totalSteps: Int = 0,
    val completedSteps: Int = 0,
    val startTime: String = "",
    val endTime: String = ""
)

@HiltViewModel
class MyActRecordViewModel @Inject constructor(
    private val repo: MyActRecordRepository
) : ViewModel() {

    private val _today = MutableStateFlow<List<MyActRecordUi>>(emptyList())
    val today: StateFlow<List<MyActRecordUi>> = _today

    private val _recent = MutableStateFlow<List<MyActRecordUi>>(emptyList())
    val recent: StateFlow<List<MyActRecordUi>> = _recent

    private val _all = MutableStateFlow<List<MyActRecordUi>>(emptyList())
    val all: StateFlow<List<MyActRecordUi>> = _all

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadToday() {
        if (_loading.value) return
        _loading.value = true
        viewModelScope.launch {
            runCatching { repo.getTodayLogs() }
                .onSuccess { list ->
                    val serverUi = list.map { it.toUi() }
                    // 서버 데이터와 메모리 데이터를 합침
                    val memoryToday = _today.value.filter { it.id.startsWith("completed_") }
                    val combinedToday = memoryToday + serverUi
                    _today.value = combinedToday
                }
                .onFailure { e -> _error.value = e.message }
            _loading.value = false
        }
    }

    fun loadRecent() {
        viewModelScope.launch {
            runCatching { repo.getRecentLogs() }
                .onSuccess { list -> 
                    val serverUi = list.map { it.toUi() }
                    // 서버 데이터와 메모리 데이터를 합침
                    val memoryRecent = _recent.value.filter { it.id.startsWith("completed_") }
                    val combinedRecent = memoryRecent + serverUi
                    _recent.value = combinedRecent
                }
                .onFailure { _error.value = it.message }
        }
    }

    private var allCursor: MyActRecordCursor? = null
    private var allHasNext: Boolean = true
    private val _allLoading = MutableStateFlow(false)
    val allLoading: StateFlow<Boolean> = _allLoading  // 필요 시 화면에서 사용

    fun loadAllFirst(size: Int = 24) {
        if (_allLoading.value) return
        _allLoading.value = true
        viewModelScope.launch {
            runCatching { repo.getLogs(size = size) }
                .onSuccess { page ->
                    _all.value = page.items.map { it.toUi() }
                    allCursor = page.nextCursor
                    allHasNext = page.hasNext
                }
                .onFailure { _error.value = it.message }
            _allLoading.value = false
        }
    }

    fun loadAllNext(size: Int = 24) {
        if (_allLoading.value || !allHasNext) return
        val c = allCursor ?: return
        _allLoading.value = true
        viewModelScope.launch {
            runCatching { repo.getLogs(createdAt = c.createdAt, logId = c.logId, size = size) }
                .onSuccess { page ->
                    _all.value = _all.value + page.items.map { it.toUi() }
                    allCursor = page.nextCursor
                    allHasNext = page.hasNext
                }
                .onFailure { _error.value = it.message }
            _allLoading.value = false
        }
    }

    // completed_ 루틴을 ID로 찾는 함수
    fun findCompletedRoutineById(id: String): MyActRecordUi? {
        val allRoutines = _today.value + _recent.value + _all.value
        return allRoutines.find { it.id == id }
    }
    
    // 완료된 루틴을 내 기록에 추가
    fun addCompletedRoutine(
        title: String, 
        tags: List<String>, 
        time: String = "00:00:00",
        stepNames: List<String> = emptyList(),
        stepDurations: List<String> = emptyList(),
        category: String = "",
        startTime: String = "",
        endTime: String = ""
    ) {
        Log.d(
            "MyActRecordViewModel",
            "🔄 완료 루틴 추가 요청: tagCount=${tags.size}, stepCount=${stepNames.size}, " +
                "stepDurationCount=${stepDurations.size}, duration=$time"
        )
        
        // 시간 문자열을 초로 변환
        val durationSec = try {
            val parts = time.split(":")
            when (parts.size) {
                3 -> { // HH:MM:SS
                    val hours = parts[0].toLong()
                    val minutes = parts[1].toLong()
                    val seconds = parts[2].toLong()
                    hours * 3600 + minutes * 60 + seconds
                }
                2 -> { // MM:SS
                    val minutes = parts[0].toLong()
                    val seconds = parts[1].toLong()
                    minutes * 60 + seconds
                }
                else -> 0L
            }
        } catch (e: Exception) {
            Log.e(
                "MyActRecordViewModel",
                "시간 파싱 실패: exception=${e.javaClass.simpleName}"
            )
            0L
        }
        
        Log.d("MyActRecordViewModel", "⏱️ 변환된 durationSec: $durationSec")
        
        val newRecord = MyActRecordUi(
            id = "completed_${System.currentTimeMillis()}",
            title = title,
            tags = tags,
            isComplete = true,
            startedAt = LocalDate.now(),
            durationSec = if (durationSec < 60) durationSec * 60 else durationSec,  // 1분 미만이면 60배로 늘림
            stepNames = stepNames,
            stepDurations = stepDurations,
            category = category,
            totalSteps = stepNames.size,
            completedSteps = stepNames.size, // 완료된 루틴이므로 모든 단계 완료
            startTime = startTime,
            endTime = endTime
        )
        
        Log.d(
            "MyActRecordViewModel",
            "📝 기록 생성 완료: isComplete=${newRecord.isComplete}, " +
                "stepCount=${newRecord.totalSteps}, durationSec=${newRecord.durationSec}"
        )
        
                val currentTodayList = _today.value.toMutableList()
        val currentRecentList = _recent.value.toMutableList()
        val currentAllList = _all.value.toMutableList()

        Log.d("MyActRecordViewModel", "📊 현재 오늘 루틴 개수: ${currentTodayList.size}")
        Log.d("MyActRecordViewModel", "📊 현재 최근 7일 루틴 개수: ${currentRecentList.size}")
        Log.d("MyActRecordViewModel", "📊 현재 전체 루틴 개수: ${currentAllList.size}")

        // 맨 앞에 추가
        currentTodayList.add(0, newRecord)
        currentRecentList.add(0, newRecord)
        currentAllList.add(0, newRecord)

        _today.value = currentTodayList
        _recent.value = currentRecentList
        _all.value = currentAllList
        
        Log.d("MyActRecordViewModel", "✅ 루틴 추가 완료. 새로운 오늘 개수: ${_today.value.size}")
        Log.d("MyActRecordViewModel", "✅ 루틴 추가 완료. 새로운 전체 개수: ${_all.value.size}")
    }
}

private fun MyActRecord.toUi(): MyActRecordUi =
    MyActRecordUi(
        id = id,
        title = title,
        tags = tags,
        isComplete = isCompleted,
        startedAt = startedAt,
        durationSec = durationSec
    )
