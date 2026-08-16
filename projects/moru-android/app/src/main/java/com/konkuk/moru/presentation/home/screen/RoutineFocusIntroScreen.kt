package com.konkuk.moru.presentation.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.konkuk.moru.R
import com.konkuk.moru.data.dto.response.Routine.RoutineDetailResponseV1
import com.konkuk.moru.presentation.home.RoutineStepData
import com.konkuk.moru.presentation.home.component.RoutineHeaderBox
import com.konkuk.moru.presentation.home.component.RoutineStepItem
import com.konkuk.moru.presentation.home.viewmodel.HomeRoutinesViewModel
import com.konkuk.moru.presentation.routinefocus.viewmodel.SharedRoutineViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

val sampleSteps = listOf(
    RoutineStepData("샤워하기", 15, true),
    RoutineStepData("청소하기", 10, true),
    RoutineStepData("밥먹기", 30, true),
    RoutineStepData("옷갈아입기", 8, true)
)

//메인 화면
@Composable
fun RoutineFocusIntroScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedRoutineViewModel,
    onStartClick: (selectedSteps: List<RoutineStepData>, title: String, hashTag: String, category: String, totalDuration: Int) -> Unit,
    onBackClick: () -> Unit,
) {
    
    // HomeRoutinesViewModel 주입
    val homeViewModel: HomeRoutinesViewModel = hiltViewModel()
    
    // 받아올 정보들
    val routineTitle = sharedViewModel.routineTitle.collectAsState<String>().value
    val hashTagList = sharedViewModel.routineTags.collectAsState<List<String>>().value
    val category = sharedViewModel.routineCategory.collectAsState<String>().value
    val isSimple = sharedViewModel.isSimple.collectAsState<Boolean>().value
    val steps = sharedViewModel.selectedSteps.collectAsState<List<RoutineStepData>>().value
    val routineImageUrl = sharedViewModel.routineImageUrl.collectAsState<String?>().value

    val routineDetail: RoutineDetailResponseV1? =
        homeViewModel.routineDetail.collectAsState().value
    val hashTag = hashTagList.joinToString(" ") { "#$it" }
    
    // 루틴 상세 정보가 로드되면 스텝 정보를 SharedViewModel에 설정
    LaunchedEffect(routineDetail) {
        routineDetail?.let { detail ->
            if (detail.steps.isNotEmpty()) {
                sharedViewModel.setStepsFromServer(detail.steps)
            }
        }
    }

    // Context 가져오기
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("routine_intro_prefs", android.content.Context.MODE_PRIVATE)
    val gson = Gson()
    
    // 각 루틴의 상태를 기억할 수 있또록 상태로 복사해서 관리
    var stepStates by remember { mutableStateOf(emptyList<RoutineStepData>()) }
    
    // intro 화면을 본 적이 있는지 확인하고, 있다면 저장된 스텝 상태 복원
    LaunchedEffect(steps, routineTitle) {
        
        val hasSeenIntro = sharedPreferences.getBoolean("has_seen_intro_$routineTitle", false)
        val savedStepStatesJson = sharedPreferences.getString("saved_steps_$routineTitle", null)
        
        if (hasSeenIntro && savedStepStatesJson != null && steps.isNotEmpty()) {
            try {
                val type = object : TypeToken<List<RoutineStepData>>() {}.type
                val savedStepStates: List<RoutineStepData> = gson.fromJson(savedStepStatesJson, type)
                
                // 저장된 스텝 상태와 현재 스텝을 매칭하여 복원
                val restoredStepStates = steps.map { currentStep ->
                    val savedStep = savedStepStates.find { it.name == currentStep.name }
                    if (savedStep != null) {
                        currentStep.copy(isChecked = savedStep.isChecked)
                    } else {
                        currentStep
                    }
                }
                stepStates = restoredStepStates
            } catch (_: Exception) {
                stepStates = steps.map { it.copy() }
            }
        } else {
            stepStates = steps.map { it.copy() }
        }
    }

    // 스위치가 on인 상태의 루틴의 소요시간만 합해서 총 소요시간 계산에 반영
    val totalDurationState = remember(stepStates, category, isSimple) {
        derivedStateOf<Int> {
            val duration = if (isSimple || category == "간편") stepStates.sumOf { it.duration }
            else stepStates.filter { it.isChecked }.sumOf { it.duration }
            duration
        }
    }

    val totalDuration = totalDurationState.value

    // 하나라도 on이 되어 있다면 시작하기 버튼 활성화(총 소요시간으로 판단)
    // 간편 루틴 또는 isSimple=true: 소요시간과 관계없이 활성화
    // 집중 루틴: 선택된 루틴의 소요시간 > 0일 때만 활성화
    val isStartEnabled = when {
        isSimple || category == "간편" -> {
            true
        }
        category == "집중" -> {
            val enabled = totalDuration > 0
            enabled
        }
        category.isBlank() || category.isEmpty() -> {
            // category가 없으면 스텝이 있고 totalDuration > 0이면 집중 루틴으로 간주
            val enabled = steps.isNotEmpty() && totalDuration > 0
            enabled
        }
        else -> {
            steps.isNotEmpty() // 카테고리가 없으면 스텝이 있으면 활성화
        }
    }

    Scaffold(
        //시작하기 버튼
        bottomBar = {
            Button(
                onClick = {
                    val selected = if (isSimple || category == "간편") {
                        stepStates
                    } else {
                        stepStates.filter { it.isChecked }
                    }
                    
                    
                    // intro 화면을 본 것으로 표시
                    sharedPreferences.edit().putBoolean("has_seen_intro_$routineTitle", true).apply()
                    
                    // 선택된 스텝 상태 저장 (간편 루틴의 경우 전체 스텝 상태 저장)
                    val stepStatesToSave = if (isSimple || category == "간편") {
                        stepStates
                    } else {
                        stepStates
                    }
                    val stepStatesJson = gson.toJson(stepStatesToSave)
                    sharedPreferences.edit().putString("saved_steps_$routineTitle", stepStatesJson).apply()
                    
                    // 간편 루틴의 경우 선택 상태도 함께 저장 (모두 false로 초기화)
                    if (isSimple || category == "간편") {
                        val initialSelectedStates = stepStates.map { false }
                        val selectedStatesJson = gson.toJson(initialSelectedStates)
                        sharedPreferences.edit().putString("saved_selected_states_$routineTitle", selectedStatesJson).apply()
                    }
                    
                    onStartClick(
                        selected,
                        routineTitle,
                        hashTag,
                        category,
                        totalDuration
                    )
                },
                enabled = isStartEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isStartEnabled) colors.limeGreen else colors.veryLightGray,
                )
            ) {
                Text(
                    text = "시작하기",
                    style = typography.body_SB_16,
                    color = colors.black
                )
            }
        }
    )
    { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
//            item {
//                // 상단 상태 바
//                StatusBarMock(isDarkMode = false)
//            }
            item {
                //상단 바
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp) // TopAppBar 기본 높이
                        .background(Color.White)
                ) {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.left_arrow),
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(width = 8.dp, height = 16.dp)
                        )
                    }
                }
            }

            item {
                // 투명상자/루틴 제목/태그/타입
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "시작해볼까요?",
                        style = typography.head_EB_24.copy(fontWeight = FontWeight.SemiBold),
                        color = colors.darkGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    RoutineHeaderBox(
                        routineTitle = routineTitle,
                        tags = hashTagList,
                        category = category,
                        imageUrl = routineImageUrl
                    )
                }
                Spacer(modifier = Modifier.height(172.dp))
            }
            //STEP과 루틴들
            item {
                //STEP(해야할 루틴들)
                Text(
                    text = "STEP",
                    style = typography.body_SB_16.copy(fontWeight = FontWeight.Bold),
                    color = colors.black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            itemsIndexed(stepStates) { index, step ->
                RoutineStepItem(
                    index = index,
                    title = step.name,
                    duration = step.duration,
                    isChecked = step.isChecked,
                    showSwitch = !isSimple && category == "집중",
                    showDuration = !isSimple && category == "집중",
                    onCheckedChange = { checked ->
                        stepStates = stepStates.toMutableList().also { list ->
                            list[index] = list[index].copy(isChecked = checked)
                        }
                    }
                )

                //divider
                Divider(
                    color = colors.lightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.5.dp)
                )
            }
            // TOTAL 소요시간 섹션 (루틴 타입이 "집중"이고 간편 루틴이 아닐 경우만)
            if (category == "집중" && !isSimple) {
                item {
                    Spacer(modifier = Modifier.height(92.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        //루틴 총 소요시간
                        Text(
                            text = "TOTAL",
                            style = typography.body_SB_16,
                            color = colors.black
                        )
                        Text(
                            text = "${totalDuration}m 00s",
                            style = typography.head_EB_24,
                            color = colors.black
                        )
                        Spacer(modifier = Modifier.height(21.dp))
                    }
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(175.dp))
                }
            }

        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
)
@Composable
private fun RoutineFocusIntroScreenPreview() {
    val dummyViewModel = remember { SharedRoutineViewModel() }

    // 가짜 데이터 설정
    val sampleSteps = listOf(
        RoutineStepData("샤워하기", 15, true),
        RoutineStepData("청소하기", 10, true),
        RoutineStepData("밥먹기", 30, true),
        RoutineStepData("옷갈아입기", 8, true)
    )
    dummyViewModel.setSelectedSteps(sampleSteps)
    dummyViewModel.setRoutineCategory("집중")  // 또는 "간편"

    // 제목과 태그 설정
    dummyViewModel.setRoutineTitle("주말 아침 루틴")
    dummyViewModel.setRoutineTags(listOf("태그1", "태그2"))

    RoutineFocusIntroScreen(
        sharedViewModel = dummyViewModel,
        onStartClick = { _, _, _, _, _ -> },
        onBackClick = {}
    )
}


