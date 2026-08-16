package com.konkuk.moru.presentation.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.moru.R
import com.konkuk.moru.presentation.home.RoutineStepData
import com.konkuk.moru.presentation.home.component.RoutineResultRow
import com.konkuk.moru.presentation.home.component.RoutineSelectItem
import com.konkuk.moru.presentation.myactivity.viewmodel.InsightViewModel
import com.konkuk.moru.presentation.routinefocus.viewmodel.SharedRoutineViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

// 소요시간 포맷 함수
fun formatElapsedTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

// 간편 루틴 진입 화면
@Composable
fun RoutineSimpleRunScreen(
    sharedViewModel: SharedRoutineViewModel,
    routineId: Int,
    onDismiss: () -> Unit, // x버튼 눌렀을 시
    onFinishConfirmed: (String) -> Unit
) {
    // InsightViewModel 주입 (실천율 업데이트용)
    val insightViewModel: InsightViewModel = hiltViewModel()
    val originalRoutineId = sharedViewModel.originalRoutineId.collectAsStateWithLifecycle<String?>().value
    // intro에서 받아올 값들
    val routineTitle = sharedViewModel.routineTitle.collectAsStateWithLifecycle<String>().value
    val hashTagList = sharedViewModel.routineTags.collectAsStateWithLifecycle<List<String>>().value
    val steps = sharedViewModel.selectedSteps.collectAsStateWithLifecycle<List<RoutineStepData>>().value
    val hashTag = hashTagList.joinToString(" ") { "#$it" }

    /*---------------- 상태 ----------------*/
    // Context와 SharedPreferences 가져오기
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("routine_intro_prefs", android.content.Context.MODE_PRIVATE)
    val selectionStorageKey = remember(routineId) {
        RoutineStepSelectionReducer.storageKey(routineId.toString())
    }
    val legacySelectionStorageKey = remember(routineTitle) {
        RoutineStepSelectionReducer.legacyStorageKey(routineTitle)
    }

    var selectedStates by remember(routineId) { mutableStateOf(emptyList<Boolean>()) }

    // The Flow may emit an empty list before its data arrives; render against an aligned
    // immutable snapshot so the current step index is always safe in that transition.
    val currentSelectedStates = RoutineStepSelectionReducer.reconcile(
        stepCount = steps.size,
        selection = selectedStates
    )

    LaunchedEffect(selectionStorageKey, legacySelectionStorageKey, steps.size) {
        if (steps.isEmpty()) {
            selectedStates = emptyList()
            return@LaunchedEffect
        }

        val currentSelection = sharedPreferences.getString(selectionStorageKey, null)
        val legacySelection = if (currentSelection == null) {
            sharedPreferences.getString(legacySelectionStorageKey, null)
        } else {
            null
        }
        val savedSelection = currentSelection ?: legacySelection
        selectedStates = RoutineStepSelectionReducer.restore(
            stepCount = steps.size,
            serializedSelection = savedSelection
        )

        // Move a valid legacy location to the ID-based key without keeping a title collision.
        if (legacySelection != null) {
            sharedPreferences.edit {
                putString(
                    selectionStorageKey,
                    RoutineStepSelectionReducer.serialize(selectedStates)
                )
                remove(legacySelectionStorageKey)
            }
        }
    }

    // Finish 버튼의 상태 저장
    val isAnySelected = currentSelectedStates.any { it }

    // 팝업 표시 여부
    var showFinishPopup by remember { mutableStateOf(false) }

    // 루틴 시작 시간 저장
    val routineStartTime = remember { System.currentTimeMillis() }

    // 종료 버튼 상태 저장
    var showResultPopup by remember { mutableStateOf(false) }

    //최종 소요시간 저장용
    var finalElapsedTime by remember { mutableStateOf("") }

    //컴포넌트들 배치
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp)
        ) {
            // X 버튼
            Text(
                text = "✕",
                color = colors.black,
                style = typography.body_SB_16,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { onDismiss() }
            )

            Spacer(modifier = Modifier.height(77.dp))

            // 루틴 제목 및 해시태그
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Image(
                        painter = painterResource(R.drawable.transparentbox),
                        contentDescription = "투명 박스",
                        modifier = Modifier
                            .width(53.dp)
                            .height(52.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = routineTitle,
                            style = typography.head_EB_24,
                            color = colors.black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = hashTag,
                            style = typography.body_SB_16,
                            color = colors.darkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(129.dp))

            // 루틴 항목들 (스크롤 가능)
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // 남은 공간을 모두 차지
                    .padding(end = 16.dp), // 오른쪽 패딩 추가
                contentPadding = PaddingValues(bottom = 150.dp) // 하단에 충분한 패딩 추가
            ) {
                itemsIndexed(steps) { index, step ->
                    RoutineSelectItem(
                        text = step.name,
                        isSelected = currentSelectedStates[index],
                        onClick = {
                            val newSelectedStates = RoutineStepSelectionReducer.toggle(
                                selection = currentSelectedStates,
                                index = index
                            )
                            selectedStates = newSelectedStates
                            
                            
                            // 선택 상태 변경 시 저장
                            try {
                                val selectedStatesJson =
                                    RoutineStepSelectionReducer.serialize(newSelectedStates)
                                sharedPreferences.edit {
                                    putString(selectionStorageKey, selectedStatesJson)
                                    remove(legacySelectionStorageKey)
                                }
                            } catch (_: Exception) {
                            }
                        },
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            }
        }

        // Finish 버튼 (절대 좌표 배치)
        Box(
            modifier = Modifier
                .offset(x = 221.dp, y = 387.dp)
                .width(139.dp)
                .height(123.dp)
                .clickable(
                    enabled = isAnySelected,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showFinishPopup = true
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 체크 박스
                Box(
                    modifier = Modifier
                        .width(133.dp)
                        .height(88.dp)
                        .padding(horizontal = 32.dp, vertical = 7.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isAnySelected)
                                R.drawable.enable_check_icon
                            else
                                R.drawable.disable_check_icon
                        ),
                        contentDescription = "체크 아이콘",
                        modifier = Modifier.size(74.dp),
                        tint = if (isAnySelected) colors.limeGreen else Color.Unspecified
                    )
                }
                //Finish 텍스트
                Box(
                    modifier = Modifier
                        .width(133.dp)
                        .height(35.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FINISH!",
                        style = typography.body_SB_16.copy(fontWeight = FontWeight.Bold),
                        color = colors.black
                    )
                }
            }
        }

        // Finish 팝업(팝업 1)
        if (showFinishPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .width(264.dp)
                        .height(140.dp)
                        .padding(vertical = 6.dp, horizontal = 7.72.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(21.25.dp))
                    Text(
                        text = "루틴을 종료하시겠습니까?",
                        style = typography.title_B_20.copy(fontWeight = FontWeight.SemiBold),
                        color = colors.black
                    )
                    Spacer(modifier = Modifier.height(5.79.dp))
                    Text(
                        text = "종료한 루틴은 내활동에 저장됩니다.",
                        style = typography.title_B_12,
                        color = colors.darkGray
                    )
                    Spacer(modifier = Modifier.height(19.79.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 돌아가기 버튼
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(colors.lightGray)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showFinishPopup = false
                                }
                                .width(123.dp)
                                .height(40.55.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "돌아가기",
                                style = typography.body_SB_16.copy(fontWeight = FontWeight.Bold),
                                color = colors.mediumGray
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))

                        // 종료 버튼
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(colors.limeGreen)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showResultPopup = true
                                    showFinishPopup = false
                                    finalElapsedTime =
                                        formatElapsedTime(System.currentTimeMillis() - routineStartTime)
                                }
                                .width(123.dp)
                                .height(40.55.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "종료",
                                style = typography.body_SB_16.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 2번째 팝업
        if (showResultPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .width(264.dp)
                        .height(276.dp)
                        .padding(horizontal = 6.02.dp, vertical = 5.98.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(14.09.dp))
                    Text(
                        text = "루틴 종료!",
                        style = typography.title_B_20.copy(fontWeight = FontWeight.SemiBold),
                        color = colors.black
                    )
                    Spacer(modifier = Modifier.height(20.07.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(
                                top = 8.03.dp,
                                start = 15.91.dp,
                                end = 12.93.dp,
                                bottom = 8.03.dp
                            )
                    ) {
                        Column() {
                            RoutineResultRow(R.drawable.schedule_icon, "루틴", routineTitle)
                            Spacer(modifier = Modifier.height(16.06.dp))
                            RoutineResultRow(R.drawable.check_icon_gray, "결과", "완료")
                            Spacer(modifier = Modifier.height(16.06.dp))
                            RoutineResultRow(
                                R.drawable.step_icon,
                                "스텝",
                                "${currentSelectedStates.count { it }}/${steps.size}"
                            )
                            Spacer(modifier = Modifier.height(16.06.dp))
                            RoutineResultRow(R.drawable.clock_icon, "시간", finalElapsedTime)
                        }
                    }
                    Spacer(modifier = Modifier.height(29.11.dp))
                    //확인 버튼
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(colors.limeGreen)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                showResultPopup = false
                                
                                // 간편 루틴 완료 시 실천율 업데이트
                                originalRoutineId?.let { routineId ->
                                    // 실천율 업데이트 API 호출
                                    insightViewModel.completeRoutine(routineId)
                                }
                                
                                // 루틴 완료 시 저장된 상태들 모두 초기화 (처음 상태로 복원)
                                try {
                                    sharedPreferences.edit {
                                        remove(selectionStorageKey)
                                        remove(legacySelectionStorageKey)
                                        remove("has_seen_intro_$routineTitle") // intro 다시 보도록 초기화
                                    }
                                } catch (_: Exception) {
                                }
                                
                                onFinishConfirmed(routineId.toString())
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "확인",
                            style = typography.body_SB_16,
                            color = Color.White
                        )
                    }
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
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
)
@Composable
private fun RoutineSimpleRunScreenPreview() {
    val dummyViewModel = remember { SharedRoutineViewModel() }

    // 더미 데이터 설정 (스크롤 테스트용으로 훨씬 많은 steps 추가)
    val sampleSteps = listOf(
        RoutineStepData("샤워하기", 15, true),
        RoutineStepData("청소하기", 10, true),
        RoutineStepData("밥먹기", 30, true),
        RoutineStepData("옷갈아입기", 8, true),
        RoutineStepData("이불 정리하기", 5, true),
        RoutineStepData("창문 열기", 2, true),
        RoutineStepData("커피 내리기", 8, true),
        RoutineStepData("신문 읽기", 15, true),
        RoutineStepData("운동하기", 20, true),
        RoutineStepData("일기 쓰기", 10, true),
        RoutineStepData("화분에 물주기", 3, true),
        RoutineStepData("우산 정리하기", 2, true),
        RoutineStepData("신발 정리하기", 4, true),
        RoutineStepData("가방 정리하기", 5, true),
        RoutineStepData("전화 충전하기", 1, true),
        RoutineStepData("알람 설정하기", 2, true),
        RoutineStepData("책상 정리하기", 7, true),
        RoutineStepData("컴퓨터 켜기", 1, true),
        RoutineStepData("이메일 확인하기", 5, true),
        RoutineStepData("일정 체크하기", 3, true),
        RoutineStepData("물 마시기", 1, true),
        RoutineStepData("스트레칭하기", 8, true),
        RoutineStepData("명상하기", 15, true),
        RoutineStepData("음악 듣기", 10, true),
        RoutineStepData("친구에게 연락하기", 5, true),
        RoutineStepData("가족과 대화하기", 12, true),
        RoutineStepData("취미 활동하기", 25, true),
        RoutineStepData("독서하기", 20, true),
        RoutineStepData("일기 정리하기", 8, true),
        RoutineStepData("내일 준비하기", 10, true),
        RoutineStepData("잠자리 준비하기", 15, true),
        RoutineStepData("방 청소하기", 18, true),
        RoutineStepData("빨래하기", 25, true),
        RoutineStepData("설거지하기", 12, true),
        RoutineStepData("쓰레기 버리기", 5, true),
        RoutineStepData("장보기", 45, true),
        RoutineStepData("요리하기", 60, true),
        RoutineStepData("정리정돈하기", 20, true),
        RoutineStepData("계획 세우기", 15, true),
        RoutineStepData("목표 설정하기", 10, true),
        RoutineStepData("자기계발하기", 30, true),
        RoutineStepData("새로운 기술 배우기", 40, true),
        RoutineStepData("프로젝트 진행하기", 90, true),
        RoutineStepData("회의 준비하기", 25, true),
        RoutineStepData("보고서 작성하기", 35, true),
        RoutineStepData("데이터 분석하기", 50, true),
        RoutineStepData("코딩하기", 120, true),
        RoutineStepData("디자인하기", 80, true),
        RoutineStepData("마케팅 전략 세우기", 45, true),
        RoutineStepData("고객 관리하기", 30, true),
        RoutineStepData("팀 빌딩하기", 60, true),
        RoutineStepData("리더십 개발하기", 40, true),
        RoutineStepData("커뮤니케이션 연습하기", 25, true),
        RoutineStepData("프레젠테이션 연습하기", 35, true),
        RoutineStepData("협상 연습하기", 20, true),
        RoutineStepData("문제 해결하기", 55, true),
        RoutineStepData("창의적 사고하기", 30, true),
        RoutineStepData("전략적 사고하기", 40, true),
        RoutineStepData("시스템 분석하기", 70, true),
        RoutineStepData("품질 관리하기", 45, true),
        RoutineStepData("리스크 관리하기", 35, true),
        RoutineStepData("예산 관리하기", 25, true),
        RoutineStepData("시간 관리하기", 15, true),
        RoutineStepData("우선순위 정하기", 10, true),
        RoutineStepData("효율성 개선하기", 50, true),
        RoutineStepData("혁신하기", 75, true),
        RoutineStepData("지속가능성 고려하기", 40, true),
        RoutineStepData("미래 계획하기", 60, true),
        RoutineStepData("성장하기", 100, true),
        RoutineStepData("학습하기", 45, true),
        RoutineStepData("연습하기", 30, true),
        RoutineStepData("테스트하기", 20, true),
        RoutineStepData("검토하기", 25, true),
        RoutineStepData("수정하기", 35, true),
        RoutineStepData("완성하기", 15, true),
        RoutineStepData("점검하기", 18, true),
        RoutineStepData("보완하기", 22, true),
        RoutineStepData("검증하기", 28, true),
        RoutineStepData("개선하기", 32, true),
        RoutineStepData("최적화하기", 45, true),
        RoutineStepData("표준화하기", 25, true),
        RoutineStepData("문서화하기", 35, true),
        RoutineStepData("교육하기", 50, true),
        RoutineStepData("멘토링하기", 40, true),
        RoutineStepData("코칭하기", 30, true),
        RoutineStepData("피드백하기", 20, true),
        RoutineStepData("평가하기", 25, true),
        RoutineStepData("측정하기", 15, true),
        RoutineStepData("분석하기", 35, true),
        RoutineStepData("리뷰하기", 20, true),
        RoutineStepData("검토하기", 25, true),
        RoutineStepData("수정하기", 30, true),
        RoutineStepData("완성하기", 20, true),
        RoutineStepData("배포하기", 15, true),
        RoutineStepData("모니터링하기", 40, true)
    )
    dummyViewModel.setRoutineTitle("주말 아침 루틴")
    dummyViewModel.setRoutineTags(listOf("태그", "태그"))
    dummyViewModel.setSelectedSteps(sampleSteps)

    RoutineSimpleRunScreen(
        sharedViewModel = dummyViewModel,
        routineId = 501,
        onDismiss = {},
        onFinishConfirmed = {}
    )
}
