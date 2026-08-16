package com.konkuk.moru.presentation.myroutines.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.konkuk.moru.R
import com.konkuk.moru.core.component.button.MoruButton
import com.konkuk.moru.core.component.chip.MoruChip
import com.konkuk.moru.core.component.routine.RoutineListItem
import com.konkuk.moru.core.component.routine.RoutineListItemWithClock
import com.konkuk.moru.presentation.myroutines.component.MyRoutineTopAppBar
import com.konkuk.moru.presentation.myroutines.component.TimePickerSheetContent
import com.konkuk.moru.presentation.myroutines.viewmodel.MyRoutinesViewModel
import com.konkuk.moru.presentation.routinefeed.component.modale.CenteredInfoDialog
import com.konkuk.moru.presentation.routinefeed.component.modale.CustomDialog
import com.konkuk.moru.presentation.routinefeed.component.tooltip.TooltipBubble
import com.konkuk.moru.presentation.routinefeed.component.tooltip.TooltipShape
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.moruFontLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

enum class SortOption { BY_TIME, LATEST, POPULAR }

data class MyRoutinesUiState(
    val selectedSortOption: SortOption = SortOption.BY_TIME,
    val selectedDay: DayOfWeek? = null,
    val isDeleteMode: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showInfoTooltip: Boolean = false,
    val editingRoutineId: String? = null,
    val showDeleteSuccessDialog: Boolean = false,

    // ✅ TimePicker 초기화용
    val editingScheduleId: String? = null,
    val initialTimeForSheet: LocalTime? = null,
    val initialDaysForSheet: Set<DayOfWeek> = emptySet(),
    val initialAlarmForSheet: Boolean = true,

    )

/**
 * MyRoutinesScreen의 메인 컴포저블입니다.
 * ViewModel을 직접 주입받아 UI 상태와 이벤트를 처리합니다.
 *
 * @param modifier Modifier
 * @param viewModel MyRoutinesViewModel의 인스턴스
 * @param onNavigateToCreateRoutine '루틴 만들기' 클릭 시 호출될 콜백
 * @param onNavigateToRoutineFeed '루틴피드 보기' 클릭 시 호출될 콜백
 * @param onNavigateToDetail 루틴 아이템 클릭 시 상세 화면으로 이동하기 위한 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRoutinesScreen(
    modifier: Modifier = Modifier,
    viewModel: MyRoutinesViewModel,
    onNavigateToCreateRoutine: () -> Unit,
    onNavigateToRoutineFeed: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val routinesToDisplay by viewModel.routinesToDisplay.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val checkedRoutinesCount = routinesToDisplay.count { it.isChecked }

    LaunchedEffect(Unit) {
        viewModel.refreshRoutines()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.padding(bottom = 80.dp),
            topBar = {
                MyRoutineTopAppBar(
                    onInfoClick = viewModel::onShowInfoTooltip,
                    onTrashClick = viewModel::onTrashClick,
                    selectedDay = uiState.selectedDay,
                    onDaySelected = viewModel::onDaySelected
                )
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                // --- 정렬 옵션 칩 그룹 ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortOption.entries.forEach { option ->
                        MoruChip(
                            text = when (option) {
                                SortOption.BY_TIME -> "시간순"
                                SortOption.LATEST -> "최신순"
                                SortOption.POPULAR -> "인기순"
                            },
                            isSelected = uiState.selectedSortOption == option,
                            onClick = { viewModel.onSortOptionSelected(option) },
                            selectedBackgroundColor = Color(0xFF555555),
                            selectedContentColor = Color.White,
                            unselectedBackgroundColor = Color(0xFFF0F0F0),
                            unselectedContentColor = Color(0xFF888888)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    if (uiState.isDeleteMode) {
                        MoruButton(
                            text = "삭제하기",
                            textStyle = MORUTheme.typography.desc_M_12,
                            onClick = viewModel::showDeleteDialog,
                            enabled = checkedRoutinesCount > 0,
                            backgroundColor = MORUTheme.colors.limeGreen,
                            contentColor = Color.Black
                        )
                    }
                }

                // --- 루틴 목록 또는 빈 화면 표시 ---
                if (routinesToDisplay.isEmpty()) {
                    EmptyMyRoutineView(onNavigateToCreateRoutine, onNavigateToRoutineFeed)
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = routinesToDisplay, key = { it.routineId }) { routine ->
                            if (uiState.isDeleteMode) {
                                RoutineListItem(
                                    isRunning = routine.isRunning,
                                    routineName = routine.title,
                                    tags = routine.tags,
                                    likeCount = routine.likes,
                                    isLiked = routine.isLiked,
                                    showCheckbox = true,
                                    isChecked = routine.isChecked,
                                    onCheckedChange = { isChecked ->
                                        viewModel.onCheckRoutine(routine.routineId, isChecked)
                                    },
                                    onItemClick = {
                                        viewModel.onCheckRoutine(
                                            routine.routineId,
                                            !routine.isChecked
                                        )
                                    }
                                )
                            } else {
                                RoutineListItemWithClock(
                                    isRunning = routine.isRunning,
                                    routineName = routine.title,
                                    tags = routine.tags,
                                    likeCount = routine.likes,
                                    isLiked = routine.isLiked,
                                    onLikeClick = { viewModel.onLikeClick(routine.routineId) },
                                    onClockClick = { viewModel.openTimePicker(routine.routineId) },
                                    onItemClick = {
                                        onNavigateToDetail(routine.routineId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 정보 툴팁 ---
        if (uiState.showInfoTooltip) {
            LaunchedEffect(Unit) {
                delay(3000L)
                viewModel.onDismissInfoTooltip()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 118.dp, end = 16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                TooltipBubble(
                    modifier = Modifier.width(328.dp),
                    shape = TooltipShape(cornerRadius = 8.dp, tailHorizontalOffsetFromEnd = 38.dp),
                    backgroundColor = Color.Black,
                    tailHeight = 11.5.dp
                ) {
                    Text(
                        text = "시간대가 설정되지 않은 루틴은 인사이트 분석 시에 제외됩니다.정확한 인사이트를 원한다면 시간대를 설정해보세요!",
                        color = Color.White,
                        fontFamily = moruFontLight,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        maxLines = 2,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }

    val editingId = uiState.editingRoutineId
    if (editingId != null) {
        ModalBottomSheet(
            onDismissRequest = viewModel::closeTimePicker,
            sheetState = sheetState
        ) {
            TimePickerSheetContent(
                initialTime = uiState.initialTimeForSheet,          // ✅ 추가
                initialDays = uiState.initialDaysForSheet,          // ✅ 추가
                initialAlarm = uiState.initialAlarmForSheet,        // ✅ 추가
                onConfirm = { time, days, alarm ->
                    viewModel.onConfirmTimeSet(editingId, time, days, alarm)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) viewModel.closeTimePicker()
                    }
                }
            )
        }
    }

    // --- 삭제 확인 다이얼로그 ---
    if (uiState.showDeleteDialog) {
        CustomDialog(
            onDismissRequest = viewModel::dismissDeleteDialog,
            onConfirmation = viewModel::deleteCheckedRoutines,
            content = {
                Text(
                    text = "루틴을 삭제하시겠습니까?",
                    style = MORUTheme.typography.title_B_20,
                    textAlign = TextAlign.Center
                )
            }
        )
    }

    // --- 삭제 완료 다이얼로그 ---
    if (uiState.showDeleteSuccessDialog) {
        LaunchedEffect(Unit) {
            delay(2000L)
            viewModel.dismissDeleteSuccessDialog()
        }
        CenteredInfoDialog(
            onDismissRequest = viewModel::dismissDeleteSuccessDialog,
            dialogColor = Color(0xFF212120)
        ) {
            Text(
                text = "삭제되었습니다!",
                color = Color.White,
                style = MORUTheme.typography.desc_M_14
            )
        }
    }
}

/**
 * 표시할 루틴이 없을 때 보여주는 컴포저블
 */
@Composable
private fun EmptyMyRoutineView(
    onNavigateToCreateRoutine: () -> Unit,
    onNavigateToRoutineFeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_empty_routine_box),
            contentDescription = "루틴 없음",
            modifier = Modifier.size(103.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(44.dp))
        Text(
            text = "아직 내 루틴이 비어있어요.",
            style = MORUTheme.typography.desc_M_20,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = "당신만의 루틴을 직접 만들거나,\n다른 사람의 루틴을 참고해보세요!",
            style = MORUTheme.typography.desc_M_16,
            color = MORUTheme.colors.darkGray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(34.dp))
        Text(
            text = "루틴 만들기",
            style = MORUTheme.typography.desc_M_16,
            color = Color(0xFF407196),
            modifier = Modifier.clickable { onNavigateToCreateRoutine() }
        )
        Spacer(modifier = Modifier.height(11.dp))
        Text(
            text = "루틴피드 보기",
            style = MORUTheme.typography.desc_M_16,
            color = Color(0xFF407196),
            modifier = Modifier.clickable { onNavigateToRoutineFeed() }
        )
    }
}


// --- Previews ---
@Preview(showBackground = true, name = "내 루틴 - 기본 모드 (루틴 있음)")
@Composable
private fun MyRoutinesScreenPreview() {
    MORUTheme {
        // 프리뷰에서는 viewModel()을 직접 호출하여 사용합니다.
        MyRoutinesScreen(
            viewModel = viewModel(),
            onNavigateToCreateRoutine = {},
            onNavigateToRoutineFeed = {},
            onNavigateToDetail = {}
        )
    }
}

@Preview(showBackground = true, name = "내 루틴 - 삭제 모드")
@Composable
private fun MyRoutinesScreenDeleteModePreview() {
    val previewViewModel: MyRoutinesViewModel = viewModel()
    // 삭제 모드를 활성화하기 위해 ViewModel의 상태를 직접 조작 (프리뷰용)
    LaunchedEffect(Unit) {
        previewViewModel.onTrashClick()
    }
    MORUTheme {
        MyRoutinesScreen(
            viewModel = viewModel(),
            onNavigateToCreateRoutine = {},
            onNavigateToRoutineFeed = {},
            onNavigateToDetail = {}
        )
    }
}

@Preview(showBackground = true, name = "내 루틴 - 비어있을 때")
@Composable
private fun MyRoutinesScreenEmptyPreview() {
    val previewViewModel: MyRoutinesViewModel = viewModel()
    // 루틴 목록을 비우기 위해 ViewModel의 데이터를 직접 조작 (프리뷰용)
    LaunchedEffect(Unit) {
        // 실제 앱에서는 이런 방식은 좋지 않지만, 프리뷰를 위해 임시로 사용합니다.
        // viewModel.loadRoutines()를 재정의하거나, 테스트용 Fakes/Mocks를 사용하는 것이 더 좋습니다.
        // 여기서는 간단하게 표현하기 위해 직접 상태를 변경하는 것처럼 가정합니다.
    }
    MORUTheme {
        MyRoutinesScreen(
            viewModel = viewModel(),
            onNavigateToCreateRoutine = {},
            onNavigateToRoutineFeed = {},
            onNavigateToDetail = {}
        )
    }
}

