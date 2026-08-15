package com.konkuk.moru.presentation.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.presentation.home.FabConstants
import com.konkuk.moru.presentation.home.RoutineStepData
import com.konkuk.moru.presentation.home.component.HomeFloatingActionButton
import com.konkuk.moru.presentation.home.component.HomeTopAppBar
import com.konkuk.moru.presentation.home.component.RoutineCardList
import com.konkuk.moru.presentation.home.component.TodayRoutinePager
import com.konkuk.moru.presentation.home.component.TodayWeekTab
import com.konkuk.moru.presentation.home.component.WeeklyCalendarView
import com.konkuk.moru.presentation.home.viewmodel.HomeRoutinesViewModel
import com.konkuk.moru.presentation.home.viewmodel.UserViewModel
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.presentation.routinefocus.viewmodel.SharedRoutineViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import com.konkuk.moru.core.datastore.SchedulePreference
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.first
import kotlin.collections.isNotEmpty
import kotlin.collections.mapNotNull

fun convertDurationToMinutes(duration: String): Int {
    val parts = duration.split(":")
    val minutes = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return minutes + (seconds / 60)
}

// 라벨 포맷(루틴 제목을 최대 10글자로 제한하고 4글자씩 줄바꿈, 최대 3줄)
private fun Routine.toCalendarLabel(): String {
    val title = this.title.take(10) // 최대 10글자로 제한

    return when {
        // 4글자 이하면 그대로 사용
        title.length <= 4 -> title
        // 5-8글자면 4글자씩 2줄로 줄바꿈
        title.length <= 8 -> {
            val firstLine = title.take(4)
            val secondLine = title.drop(4)
            "$firstLine\n$secondLine"
        }
        // 9-10글자면 4글자씩 3줄로 줄바꿈
        else -> {
            val firstLine = title.take(4)
            val secondLine = title.take(8).drop(4)
            val thirdLine = title.drop(8)
            "$firstLine\n$secondLine\n$thirdLine"
        }
    }
}

// 이번주(월~일) 맵 생성: dayOfMonth -> [라벨, 라벨, ...]
private fun buildWeeklyMap(routines: List<Routine>): Pair<Map<Int, List<String>>, Int> {
    val today = LocalDate.now()
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    val map = weekDates.associate { date ->

        val labels = routines
            .filter { r ->
                // 🔸 요일 세팅된 루틴만 주간에 배치
                val hasScheduledDays = r.scheduledDays.isNotEmpty()
                val containsDayOfWeek = r.scheduledDays.contains(date.dayOfWeek)

                // 더 많은 루틴을 표시하기 위한 개선된 로직
                val shouldShow = when {
                    // 1. 서버 스케줄에 scheduledDays가 설정되어 있고 해당 요일에 포함되는 경우 (우선순위 1)
                    hasScheduledDays && containsDayOfWeek -> {
                        true
                    }
                    // 2. scheduledDays가 비어있지만 오늘 요일인 경우 (우선순위 2)
                    !hasScheduledDays && date.dayOfWeek == today.dayOfWeek -> {
                        true
                    }
                    // 3. 그 외의 경우는 표시하지 않음 (임시 분산 배치 제거)
                    else -> {
                        false
                    }
                }

                shouldShow
            }
            .sortedBy { it.scheduledTime ?: LocalTime.MAX }
            .map { it.toCalendarLabel() }

        date.dayOfMonth to labels
    }

    return map to today.dayOfMonth
}

// requiredTime을 기반으로 간편/집중 루틴 구분
private fun determineRoutineType(requiredTime: String): Boolean {
    // requiredTime이 비어있으면 간편 루틴, 있으면 집중 루틴
    return requiredTime.isBlank()
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
    } catch (_: Exception) {
        0
    }
}

// 홈 메인 페이지
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeEntry: NavBackStackEntry,
    sharedViewModel: SharedRoutineViewModel,
    modifier: Modifier = Modifier,
    fabOffsetY: MutableState<Float>,
    todayTabOffsetY: MutableState<Float>,
    onShowOnboarding: () -> Unit = {},
) {

    val userVm: UserViewModel = hiltViewModel()
    val nickname by userVm.nickname.collectAsState()
    LaunchedEffect(Unit) {
        userVm.loadMe()
    }

    // Context 가져오기
    val context = LocalContext.current

    // 오늘 탭 표시용(서버 응답 + 순서 복원/완료 시 뒤로)
    val todayRoutines = remember { mutableStateListOf<Routine>() }

    // 진행중 루틴 ID 스택 수신 (Int 안정 ID 리스트)
    val runningIds by homeEntry.savedStateHandle
        .getStateFlow<List<Int>>("runningRoutineIds", emptyList())
        .collectAsState(initial = emptyList())

    // 서버 오늘 루틴
    val homeVm: HomeRoutinesViewModel = hiltViewModel()
    val routineDetail by homeVm.routineDetail.collectAsState()


    // SharedRoutineViewModel을 HomeRoutinesViewModel에 설정
    LaunchedEffect(Unit) {
        homeVm.setSharedRoutineViewModel(sharedViewModel)
    }

    // ① Today(오늘용)
    val serverRoutines by homeVm.serverRoutines.collectAsState()
    // ② 내 루틴 전체(하단 카드용)
    val myRoutines by homeVm.myRoutines.collectAsState()
    // ③ 스케줄 정보가 병합된 루틴 (주간 달력용)
    val scheduledRoutines by homeVm.scheduledRoutines.collectAsState()

    // 하이라이트 대상 보관 (진행중인 모든 루틴)
    var highlightIds by remember { mutableStateOf<List<Int>>(emptyList()) }

    // runningIds 스택 변경 시 간편 루틴만 하이라이트 ID로 설정
    LaunchedEffect(runningIds, myRoutines) {
        if (runningIds.isNotEmpty()) {
            // 스택에서 간편 루틴만 필터링하여 하이라이트 대상으로 설정
            val simpleRoutineIds = mutableListOf<Int>()

            runningIds.forEach { id ->
                val routine = myRoutines.find { it.routineId.toStableIntId() == id }
                val isSimpleRoutine = routine?.let { determineRoutineType(it.requiredTime) } ?: false

                if (isSimpleRoutine) {
                    simpleRoutineIds.add(id)
                }
            }

            highlightIds = simpleRoutineIds
        } else {
            // 스택이 비어있으면 하이라이트 해제
            if (highlightIds.isNotEmpty()) {
                highlightIds = emptyList()
            }
        }
    }



    LaunchedEffect(Unit) {
        try {
            homeVm.loadTodayRoutines()
        } catch (_: Exception) {
        }

        // 하단 카드용 전체 목록도 로드
        try {
            homeVm.loadMyRoutines()
        } catch (_: Exception) {
        }
    }

    // 화면이 다시 활성화될 때 데이터 리로드
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.route == Route.Home.route) {
                try {
                    homeVm.loadMyRoutines()

                    // 오늘 루틴도 다시 로드하여 스케줄 정보 업데이트
                    homeVm.loadTodayRoutines()
                } catch (_: Exception) {
                }
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    // 서버 데이터 로드 후 스케줄 정보와 병합
    LaunchedEffect(serverRoutines) {
        if (serverRoutines.isNotEmpty()) {

            // 로컬 스케줄 정보도 병합 (기존 기능 유지)
            homeVm.mergeWithLocalSchedule(context)

            // 스케줄 정보를 먼저 가져온 후 UI 업데이트

            val updatedRoutines = withContext(Dispatchers.IO) {
                // 각 루틴의 스케줄 정보를 병렬로 가져오기
                val scheduleJobs = serverRoutines.map { routine ->
                    async {
                        try {
                            val schedules = homeVm.getRoutineSchedules(routine.routineId)

                            if (schedules.isNotEmpty()) {
                                // 스케줄 정보를 DayOfWeek와 LocalTime으로 변환
                                val scheduledDays: Set<DayOfWeek> = schedules.mapNotNull { schedule ->
                                    val dayOfWeek = when (schedule.dayOfWeek.uppercase()) {
                                        "MON" -> DayOfWeek.MONDAY
                                        "TUE" -> DayOfWeek.TUESDAY
                                        "WED" -> DayOfWeek.WEDNESDAY
                                        "THU" -> DayOfWeek.THURSDAY
                                        "FRI" -> DayOfWeek.FRIDAY
                                        "SAT" -> DayOfWeek.SATURDAY
                                        "SUN" -> DayOfWeek.SUNDAY
                                        else -> {
                                            null
                                        }
                                    }
                                    dayOfWeek
                                }.toSet()

                                val scheduledTime = if (schedules.isNotEmpty()) {
                                    try {
                                        val time = LocalTime.parse(schedules.first().time, DateTimeFormatter.ofPattern("HH:mm:ss"))
                                        time
                                    } catch (_: Exception) {
                                        null
                                    }
                                } else null


                                routine.copy(scheduledDays = scheduledDays, scheduledTime = scheduledTime)
                            } else {
                                routine.copy(scheduledDays = emptySet(), scheduledTime = null)
                            }
                        } catch (_: Exception) {
                            routine.copy(scheduledDays = emptySet(), scheduledTime = null)
                        }
                    }
                }

                // 모든 스케줄 정보를 병렬로 가져온 후 반환
                scheduleJobs.awaitAll()
            }

            // todayRoutines를 한 번에 업데이트
            todayRoutines.clear()
            todayRoutines.addAll(updatedRoutines)

        }

        // ▼▼▼ 여기서부터는 LaunchedEffect(serverRoutines) 블록 안, serverRoutines 체크와 별개로 실행되어야 하므로
        //     if (serverRoutines.isNotEmpty()) { ... } 의 닫는 중괄호 뒤에 위치해야 합니다.
        //     여분으로 닫히던 중괄호를 제거해 문법 오류를 수정했습니다.
        // 서버 데이터 로드 후 runningIds 스택이 있으면 myRoutines에서 해당 루틴들을 isRunning=true로 설정하고 맨 앞으로 이동 (TODAY 탭은 제외)
        if (runningIds.isNotEmpty()) {

            // myRoutines에서 진행중인 루틴들을 맨 앞으로 이동 (TODAY 탭은 하이라이트/이동 없음)
            val myRoutinesList = myRoutines.toList()
            val updatedRoutines = myRoutinesList.toMutableList()

            // 스택의 순서대로 (최신이 맨 위) 진행중인 루틴들을 맨 앞으로 이동 (간편 루틴만 isRunning=true)
            val runningRoutines = mutableListOf<Routine>()

            runningIds.reversed().forEach { id ->
                val myIdx = updatedRoutines.indexOfFirst { it.routineId.toStableIntId() == id }
                if (myIdx >= 0) {
                    val routine = updatedRoutines[myIdx]
                    val isSimpleRoutine = determineRoutineType(routine.requiredTime)


                    val runningRoutine = updatedRoutines.removeAt(myIdx)
                    val updatedRunningRoutine = if (isSimpleRoutine) {
                        runningRoutine.copy(isRunning = true) // 간편 루틴만 하이라이트
                    } else {
                        runningRoutine.copy(isRunning = false) // 집중 루틴은 하이라이트 안함
                    }
                    runningRoutines.add(updatedRunningRoutine)

                }
            }

            // 스택 순서대로 맨 앞에 추가 (최신이 맨 위)
            runningRoutines.reversed().forEach { routine ->
                updatedRoutines.add(0, routine)
            }

            homeVm.updateMyRoutines(updatedRoutines)
        }
    }

    // 네비게이션 트리거 처리
    val navigateToRoutineFocus by homeEntry.savedStateHandle
        .getStateFlow<String?>("navigateToRoutineFocus", null)
        .collectAsState(initial = null)

    val navigateToRoutineSimpleRun by homeEntry.savedStateHandle
        .getStateFlow<String?>("navigateToRoutineSimpleRun", null)
        .collectAsState(initial = null)

    LaunchedEffect(navigateToRoutineFocus) {
        navigateToRoutineFocus?.let {
            // 스텝 정보 로드 완료 후 네비게이션
            navController.navigate(Route.RoutineFocusIntro.route)
            // 트리거 초기화
            homeEntry.savedStateHandle["navigateToRoutineFocus"] = null
        }
    }

    LaunchedEffect(navigateToRoutineSimpleRun) {
        navigateToRoutineSimpleRun?.let {
            // 스텝 정보 로드 완료 후 네비게이션
            navController.navigate(Route.RoutineSimpleRun.route)
            // 트리거 초기화
            homeEntry.savedStateHandle["navigateToRoutineSimpleRun"] = null
        }
    }

    // routineDetail이 로드되면 스텝 정보를 SharedRoutineViewModel에 설정
    LaunchedEffect(routineDetail) {
        val detail = routineDetail
        if (detail != null) {
            // requiredTime을 함께 전달
            val currentRoutine = todayRoutines.find { it.routineId == detail.id }
            val requiredTime = currentRoutine?.requiredTime ?: ""
            sharedViewModel.setStepsFromServer(detail.steps, requiredTime)

            // category도 함께 설정
            if (detail.category?.isNotBlank() == true && detail.category != "없음") {
                sharedViewModel.setRoutineCategory(detail.category)
            }
        }
    }

    //탭 선택 상태(오늘,이번주)
    var selectedTab by remember { mutableStateOf(0) }

    val finishedId by homeEntry.savedStateHandle
        .getStateFlow<String?>("finishedRoutineId", null)
        .collectAsState(initial = null)

    val savedOrderIds by homeEntry.savedStateHandle
        .getStateFlow<List<String>>("todayOrderIds", emptyList())
        .collectAsState(initial = emptyList())

    // 서버 응답이 들어오면: 저장된 순서(todayOrderIds)로 복원, 없으면 시간순 정렬
    LaunchedEffect(serverRoutines, savedOrderIds) {
        if (serverRoutines.isEmpty()) {
            todayRoutines.clear()
            homeEntry.savedStateHandle["todayOrderIds"] = emptyList<String>()
            return@LaunchedEffect
        }


        val ordered = if (savedOrderIds.isNotEmpty()) {
            val byId: Map<String, Routine> = serverRoutines.associateBy { it.routineId }
            val inSaved: List<Routine> = savedOrderIds.mapNotNull { byId[it] }
            val remaining: List<Routine> =
                serverRoutines.filter { it.routineId !in savedOrderIds.toSet() }
            inSaved + remaining
        } else {
            // 저장된 순서가 없으면 현재 시간 기준으로 가장 가까운 시간대부터 정렬
            serverRoutines.sortByNearestTime()
        }


        todayRoutines.clear()
        todayRoutines.addAll(ordered)

        // 첫 진입이면 현재 순서를 저장해 둔다 (복원용)
        if (savedOrderIds.isEmpty()) {
            val ids = ordered.map { it.routineId }
            homeEntry.savedStateHandle["todayOrderIds"] = ids
        }
    }

    // 완료 루틴 맨 뒤로 이동 + 순서 저장
    LaunchedEffect(finishedId) {
        finishedId?.let { id ->

            val idx = todayRoutines.indexOfFirst { it.routineId == id }

            if (idx >= 0) {
                val finished = todayRoutines.removeAt(idx)
                todayRoutines.add(finished)

                // 순서 저장
                val newOrderIds = todayRoutines.map { it.routineId }
                homeEntry.savedStateHandle["todayOrderIds"] = newOrderIds
            }

            // finishedId 초기화
            homeEntry.savedStateHandle["finishedRoutineId"] = null
        }
    }

    // 루틴 태그 샘플(이번주 탭 선택 시 달력 날짜에 들어갈 것들) — 기존 주석/구조 유지
    val sampleRoutineTags = mapOf(
        8 to listOf("아침 운동", "회의"),
        10 to listOf("아침 운동"),
        12 to listOf("아침 운동", "회의"),
        13 to listOf("주말아침 완전집중루틴"),
        14 to listOf("주말아침루틴")
    )

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        // FAB
        floatingActionButton = {
            HomeFloatingActionButton(
                modifier = Modifier
                    .offset(y = -FabConstants.FabTotalBottomPadding)
                    .onGloballyPositioned { layoutCoordinates ->
                        val position = layoutCoordinates.positionInRoot()
                        val size = layoutCoordinates.size
                        val centerY = position.y + size.height / 2f
                        fabOffsetY.value = centerY
                    },
                onClick = { navController.navigate(Route.RoutineCreate.route) }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->

        LaunchedEffect(todayTabOffsetY.value, fabOffsetY.value) {
            if (todayTabOffsetY.value > 0f && fabOffsetY.value > 0f) {
                onShowOnboarding()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 100.dp), // 하단 여유 공간 추가
            verticalArrangement = Arrangement.spacedBy(8.dp) // 아이템 간 간격 추가
        ) {
            item {
                //로고와 MORU
                HomeTopAppBar()
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(111.dp)
                ) {
                    // 1.인삿말
                    val displayName = nickname ?: "XX"
                    Text(
                        text = "${displayName}님,\n오늘은 어떤 루틴을 시작할까요?",
                        style = typography.title_B_20.copy(lineHeight = 30.sp),
                        color = colors.black,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 16.dp, top = 26.dp, bottom = 25.dp)
                    )
                }
            }
            item {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = colors.lightGray,
                    thickness = 1.dp
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
            item {
                Column(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        val boundsInRoot = coordinates.boundsInRoot()
                    }
                ) {
                    // 2. TODAY 텍스트
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .onGloballyPositioned { coordinates ->
                                val boundsInRoot = coordinates.boundsInRoot()
                            },
                        text = "TODAY",
                        style = typography.desc_M_16.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        ),
                        color = colors.black,
                    )

                    // 3. 월 일 요일
                    val currentDate = LocalDate.now()
                    val monthDay =
                        currentDate.format(DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN))
                    val dayOfWeek = when (currentDate.dayOfWeek.value) {
                        1 -> "월"
                        2 -> "화"
                        3 -> "수"
                        4 -> "목"
                        5 -> "금"
                        6 -> "토"
                        7 -> "일"
                        else -> ""
                    }
                    val todayText = "$monthDay $dayOfWeek"
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .onGloballyPositioned { coordinates ->
                                val boundsInRoot = coordinates.boundsInRoot()
                            },
                        text = todayText,
                        style = typography.head_EB_24.copy(lineHeight = 24.sp),
                        color = colors.black
                    )

                    // 4. 상태 텍스트 (서버 오늘 루틴 기준)
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .onGloballyPositioned { coordinates ->
                                val boundsInRoot = coordinates.boundsInRoot()
                            },
                        text = if (todayRoutines.isNotEmpty()) "정기 루틴이 있는 날이에요" else "정기 루틴이 없는 날이에요",
                        style = typography.desc_M_16.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        ),
                        color = colors.black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 5. TodayWeekTab 래퍼 Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .onGloballyPositioned { coordinates ->
                                val boundsInRoot = coordinates.boundsInRoot()
                                val centerY = boundsInRoot.center.y

                                if (centerY > 0f) {
                                    todayTabOffsetY.value = centerY
                                }
                            }
                    ) {
                        TodayWeekTab(
                            selectedTabIndex = selectedTab,
                            onTabSelected = {
                                selectedTab = it
                            }
                        )
                    }

                    // 선택된 탭에 따라 콘텐츠 분기
                    when (selectedTab) {
                        // 오늘 탭 선택 시
                        0 -> if (todayRoutines.isNotEmpty()) {
                            TodayRoutinePager(
                                routines = todayRoutines,
                                onRoutineClick = { routine, _ ->
                                    val stableId = routine.routineId.toStableIntId()

                                    // intro 화면을 본 적이 있는지 확인
                                    val hasSeenIntro = context.getSharedPreferences("routine_intro_prefs", android.content.Context.MODE_PRIVATE)
                                        .getBoolean("has_seen_intro_${routine.title}", false)


                                    sharedViewModel.setSelectedRoutineId(stableId)
                                    sharedViewModel.setOriginalRoutineId(routine.routineId)
                                    // requiredTime 기반으로 간편/집중 구분
                                    val isSimple = determineRoutineType(routine.requiredTime)
                                    val actualCategory = if (isSimple) "간편" else "집중"
                                    sharedViewModel.setRoutineInfo(title = routine.title, category = actualCategory, tags = routine.tags, isSimple = isSimple, imageUrl = routine.imageUrl)

                                    // 루틴 상세 정보 로드 (스텝 포함) 후 SharedRoutineViewModel에 직접 설정
                                    homeVm.loadMyRoutineDetail(routine.routineId)

                                    if (hasSeenIntro && isSimple) {
                                        // 이미 intro를 본 간편 루틴이면 바로 간편 루틴 화면으로 이동

                                        // 저장된 스텝 상태 복원
                                        val savedStepStatesJson = context.getSharedPreferences("routine_intro_prefs", android.content.Context.MODE_PRIVATE)
                                            .getString("saved_steps_${routine.title}", null)

                                        if (savedStepStatesJson != null) {
                                            try {
                                                val gson = com.google.gson.Gson()
                                                val type = com.google.gson.reflect.TypeToken.getParameterized(List::class.java, RoutineStepData::class.java).type
                                                val savedStepStates: List<RoutineStepData> = gson.fromJson(savedStepStatesJson, type)

                                                // SharedRoutineViewModel에 저장된 스텝 상태 설정
                                                sharedViewModel.setStepsFromSaved(savedStepStates)
                                            } catch (_: Exception) {
                                            }
                                        }

                                        // 스택에 추가
                                        val currentRunningIds = homeEntry.savedStateHandle.get<List<Int>>("runningRoutineIds") ?: emptyList()
                                        val updatedRunningIds = currentRunningIds + stableId
                                        homeEntry.savedStateHandle["runningRoutineIds"] = updatedRunningIds

                                        // 바로 간편 루틴 화면으로 네비게이션
                                        homeEntry.savedStateHandle["navigateToRoutineSimpleRun"] = routine.routineId
                                    } else {
                                        // 처음이거나 집중 루틴이면 intro 화면으로 이동
                                        homeEntry.savedStateHandle["navigateToRoutineFocus"] = routine.routineId
                                    }
                                }
                            )
                        } else {
                            // 오늘 루틴 없을 때도 Divider가 밀려 오지 않도록 고정 높이 확보
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(184.dp) // TodayRoutinePager의 전체 높이와 동일
                            )
                        }

                        // 이번주 탭 선택 시
                        1 -> {
                            // 주간 데이터 만들기 (todayRoutines 사용 - 서버 스케줄 정보가 포함됨)
                            val mergedRoutines = todayRoutines.toList()


                            val (routinesPerDate, todayDom) = buildWeeklyMap(mergedRoutines)

                            WeeklyCalendarView(
                                routinesPerDate = routinesPerDate,
                                today = todayDom
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 7.dp,
                        color = colors.lightGray
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    //루틴 목록 (오늘 루틴들 그대로 노출)
                    Row(
                        modifier = Modifier.padding(top = 3.dp, start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "루틴 목록",
                            style = typography.desc_M_16.copy(fontWeight = FontWeight.Bold),
                            color = colors.black,
                            modifier = Modifier.clickable {
                                navController.navigate(Route.MyRoutine.route)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_c),
                            contentDescription = "오른쪽 화살표",
                            modifier = Modifier.size(width = 8.dp, height = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ⬇️ 하단 카드는 "내 루틴 전체" 사용 + 우선순위 정렬
                    if (myRoutines.isNotEmpty()) {
                        val context = LocalContext.current
                        val list = myRoutines.sortedForList()   // 이미 정렬된 리스트


                        RoutineCardList(
                            routines = list,
                            onRoutineClick = { routineId: String ->

                                // 정렬된 리스트에서 클릭된 루틴 찾기
                                val routine = list.firstOrNull { it.routineId == routineId }
                                if (routine == null) {
                                    return@RoutineCardList
                                }

                                // 기존 Int API와 호환
                                val stableId = routine.routineId.toStableIntId()
                                sharedViewModel.setSelectedRoutineId(stableId)
                                sharedViewModel.setOriginalRoutineId(routine.routineId)

                                // intro 화면을 본 적이 있는지 확인
                                val hasSeenIntro = context.getSharedPreferences("routine_intro_prefs", android.content.Context.MODE_PRIVATE)
                                    .getBoolean("has_seen_intro_${routine.title}", false)

                                // requiredTime 기반으로 간편/집중 구분
                                val isSimple = determineRoutineType(routine.requiredTime)
                                val actualCategory = if (isSimple) "간편" else "집중"
                                sharedViewModel.setRoutineInfo(
                                    title = routine.title,
                                    category = actualCategory,
                                    tags = routine.tags,
                                    isSimple = isSimple
                                )

                                // 루틴 상세 정보 로드 (스텝 포함) 후 네비게이션
                                homeVm.loadMyRoutineDetail(routine.routineId)

                                if (hasSeenIntro && isSimple) {
                                    // 이미 intro를 본 간편 루틴이면 바로 간편 루틴 화면으로 이동

                                    // 저장된 스텝 상태 복원
                                    val savedStepStatesJson = context.getSharedPreferences("routine_intro_prefs", android.content.Context.MODE_PRIVATE)
                                        .getString("saved_steps_${routine.title}", null)

                                    if (savedStepStatesJson != null) {
                                        try {
                                            val gson = com.google.gson.Gson()
                                            val type = com.google.gson.reflect.TypeToken.getParameterized(List::class.java, RoutineStepData::class.java).type
                                            val savedStepStates: List<RoutineStepData> = gson.fromJson(savedStepStatesJson, type)

                                            // SharedRoutineViewModel에 저장된 스텝 상태 설정
                                            sharedViewModel.setStepsFromSaved(savedStepStates)
                                        } catch (_: Exception) {
                                        }
                                    }

                                    // 스택에 추가
                                    val currentRunningIds = homeEntry.savedStateHandle.get<List<Int>>("runningRoutineIds") ?: emptyList()
                                    val updatedRunningIds = currentRunningIds + stableId
                                    homeEntry.savedStateHandle["runningRoutineIds"] = updatedRunningIds

                                    // 바로 간편 루틴 화면으로 네비게이션
                                    homeEntry.savedStateHandle["navigateToRoutineSimpleRun"] = routine.routineId
                                } else {
                                    // 처음이거나 집중 루틴이면 intro 화면으로 이동
                                    homeEntry.savedStateHandle["navigateToRoutineFocus"] = routine.routineId
                                }
                            },
                            runningHighlightIds = highlightIds
                        )
                    }

                    // 하단 여유 공간 추가 (스크롤이 제대로 작동하도록)
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }
}

// String ID → 안정적인 Int 키 (기존 Int API/콜백용)
private fun String.toStableIntId(): Int {
    this.toLongOrNull()?.let {
        val mod = (it % Int.MAX_VALUE).toInt()
        return if (mod >= 0) mod else -mod
    }
    var h = 0
    for (ch in this) h = (h * 31) + ch.code
    return h
}

// 오늘 "루틴 목록" 전용 정렬:
// HomeRoutinesViewModel에서 이미 정렬이 완료되었으므로 여기서는 추가 정렬하지 않음
private fun List<Routine>.sortedForList(): List<Routine> {
    // HomeRoutinesViewModel에서 이미 정렬이 완료되었으므로 그대로 반환
    return this
}

// 현재 시간을 기준으로 가장 가까운 시간대의 루틴부터 정렬 (오늘 탭용)
private fun List<Routine>.sortByNearestTime(): List<Routine> {
    val now = LocalTime.now()
    return this.sortedWith(
        compareBy<Routine> { routine ->
            when {
                // 1. 진행중인 루틴 우선
                routine.isRunning -> -1
                // 2. 시간이 설정되지 않은 루틴은 맨 뒤로
                routine.scheduledTime == null -> 1
                // 3. 시간이 설정된 루틴은 현재 시간과의 차이로 정렬
                else -> {
                    val timeDiff = kotlin.math.abs(
                        java.time.Duration.between(now, routine.scheduledTime).toMinutes()
                    )
                    // 오늘 이미 지난 시간은 내일로 계산
                    val adjustedDiff = if (routine.scheduledTime < now) {
                        timeDiff + 24 * 60 // 24시간(1440분) 추가
                    } else {
                        timeDiff
                    }
                    adjustedDiff
                }
            }
        }
    )
}

// HomeScreen depends on navigation state and Hilt ViewModels. Previewable UI
// should be extracted into stateless sections instead of constructing ViewModels here.
