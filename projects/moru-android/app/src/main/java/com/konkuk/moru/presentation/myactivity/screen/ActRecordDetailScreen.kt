package com.konkuk.moru.presentation.myactivity.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.presentation.myactivity.component.RoutineDetailContent
import com.konkuk.moru.presentation.myactivity.viewmodel.MyActRecordDetailViewModel
import com.konkuk.moru.presentation.myactivity.viewmodel.MyActRecordViewModel
import com.konkuk.moru.domain.model.MyActRecordDetail
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

enum class RoutineStatus { FOCUSED, RELAXING, DONE }

data class LocalDateTimeRange(
    val start: LocalDateTime,
    val end: LocalDateTime
)

data class RoutineStep(
    val order: Int,
    val name: String,
    val startTime: LocalTime,
    val endTime: LocalTime?,
    val duration: Duration,
    val memo: String? = null
)

data class RoutineDetail(
    val title: String,
    val status: RoutineStatus,
    val tags: List<String>,
    val totalDuration: Duration,
    val result: String,
    val dateRange: LocalDateTimeRange,
    val steps: List<RoutineStep>
)

@Composable
fun ActRecordDetailScreen(
    logId: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: MyActRecordDetailViewModel = hiltViewModel()
) {
    val detail = vm.detail.collectAsState().value
    val loading = vm.loading.collectAsState().value
    val error = vm.error.collectAsState().value

    // completed_로 시작하는 ID인 경우 서버에서 로드하지 않음
    val isCompletedRoutine = logId.startsWith("completed_")
    
    LaunchedEffect(logId) { 
        if (!isCompletedRoutine) {
            vm.load(logId) 
        }
    }

    when {
        loading && detail == null && !isCompletedRoutine -> {
            Text("로딩중")
        }
        error != null && detail == null && !isCompletedRoutine -> {
            Text("error")
        }
        detail != null -> {
            val ui = remember(detail) { detail.toRoutineDetailUi() }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // 내부 컴포넌트에는 외부 modifier 전달하지 않음(레이아웃 충돌 방지)
                RoutineDetailContent(detail = ui, navController = navController)
            }
        }
        isCompletedRoutine -> {
            // completed_ 루틴의 경우 우리가 저장한 데이터를 사용
            CompletedRoutineDetailContent(logId = logId, navController = navController)
        }
    }
}

@Composable
fun CompletedRoutineDetailContent(
    logId: String,
    navController: NavHostController,
    vm: MyActRecordViewModel = hiltViewModel()
) {
    val completedRoutine = remember(logId) { vm.findCompletedRoutineById(logId) }
    
    if (completedRoutine != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 간단한 세부 정보 표시
            Text("제목: ${completedRoutine.title}")
            Text("태그: ${completedRoutine.tags.joinToString(", ")}")
            Text("소요시간: ${completedRoutine.durationSec}초")
            Text("카테고리: ${completedRoutine.category}")
            Text("시작시간: ${completedRoutine.startTime}")
            Text("종료시간: ${completedRoutine.endTime}")
            Text("총 단계: ${completedRoutine.totalSteps}")
            Text("완료된 단계: ${completedRoutine.completedSteps}")
            
            // 단계별 정보
            completedRoutine.stepNames.forEachIndexed { index, stepName ->
                val stepDuration = if (index < completedRoutine.stepDurations.size) {
                    completedRoutine.stepDurations[index]
                } else "0"
                Text("단계 ${index + 1}: $stepName (${stepDuration}분)")
            }
        }
    } else {
        Text("루틴을 찾을 수 없습니다.")
    }
}

/* ===================== 도메인 -> UI 매핑 ===================== */

// Z(UTC) 포함/미포함 모두 안전하게 파싱
private fun parseIsoToLocalDateTime(iso: String): LocalDateTime {
    // 1) Z 포함(UTC) 문자열 우선 처리
    runCatching {
        return Instant.parse(iso).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
    // 2) 로컬 ISO DateTime 보조 처리
    return runCatching { LocalDateTime.parse(iso) }
        .getOrElse { LocalDateTime.now() }
}

/* ===================== 도메인 -> UI 매핑 ===================== */

private fun MyActRecordDetail.toRoutineDetailUi(): RoutineDetail {
    // 1) 안전한 파싱 사용 (UTC/로컬 모두)
    val start = parseIsoToLocalDateTime(startedAtIso)

    // 2) totalSec 보정: 0이거나 누락이면 step(actual>0 ? actual : estimated) 합 사용
    val summedStepsSec = steps.sumOf { s ->
        val a = s.actualSec
        val e = s.estimatedSec
        (if (a > 0) a else e).coerceAtLeast(0)
    }
    val totalSeconds = if (totalSec > 0) totalSec else summedStepsSec
    val totalDur = Duration.ofSeconds(totalSeconds.coerceAtLeast(0))

    // 3) 종료시각: 응답에 있으면 파싱, 없으면 start + total
    val end = endedAtIso
        ?.let { runCatching { parseIsoToLocalDateTime(it) }.getOrNull() }
        ?: start.plusSeconds(totalDur.seconds)

    // 4) 상태 텍스트
    val status = if (isCompleted) RoutineStatus.DONE else RoutineStatus.FOCUSED
    val resultText = if (isCompleted) "완료" else "미완료"

    // 5) STEP 매핑 (있으면 그대로, 없으면 단순루틴일 때 "전체" 1스텝 생성)
    val stepUis: List<RoutineStep> =
        if (steps.isNotEmpty()) {
            var cursor = start.toLocalTime()
            steps.sortedBy { it.order }.map { s ->
                val sec = (if (s.actualSec > 0) s.actualSec else s.estimatedSec).coerceAtLeast(0)
                val dur = Duration.ofSeconds(sec)
                val st = cursor
                val ed = st.plusSeconds(dur.seconds)
                cursor = ed
                RoutineStep(
                    order = s.order,
                    name = s.name,
                    startTime = st,
                    endTime = ed,
                    duration = dur,
                    memo = s.note
                )
            }
        } else if ((isSimple || !totalDur.isZero)) {
            listOf(
                RoutineStep(
                    order = 1,
                    name = "전체",
                    startTime = start.toLocalTime(),
                    endTime = end.toLocalTime(),
                    duration = totalDur,
                    memo = null
                )
            )
        } else {
            emptyList()
        }

    return RoutineDetail(
        title = title,
        status = status,
        tags = tags.map { "#$it" },
        totalDuration = totalDur,
        result = resultText,
        dateRange = LocalDateTimeRange(start = start, end = end),
        steps = stepUis,
    )
}


