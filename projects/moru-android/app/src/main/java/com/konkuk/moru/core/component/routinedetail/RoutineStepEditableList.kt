package com.konkuk.moru.core.component.routinedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.konkuk.moru.data.model.RoutineStep
import com.konkuk.moru.data.model.RoutineStepActions
import com.konkuk.moru.presentation.myroutines.component.findNewIndex
import com.konkuk.moru.ui.theme.MORUTheme

/**
 * LazyColumn 내에서 루틴 스텝 목록과 관련 UI를 그리는 재사용 가능한 컴포넌트.
 * 드래그 앤 드롭을 포함한 모든 수정 기능을 지원합니다.
 */
fun LazyListScope.routineStepEditableList(
    steps: List<RoutineStep>,
    isEditMode: Boolean,
    listState: LazyListState,
    draggedStepIndex: Int?,
    draggedStepVerticalOffset: Float,
    headerItemCount: Int,
    actions: RoutineStepActions,
) {
    // "STEP" 타이틀
    item("step_header") {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "STEP",
                style = MORUTheme.typography.title_B_20,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }

    // 스텝 목록 (드래그 앤 드롭 로직 포함)
    itemsIndexed(items = steps, key = { _, step -> step.id }) { index, item ->
        val isDragging = draggedStepIndex == index
        val offset = if (isDragging) draggedStepVerticalOffset else 0f

        val dragHandleModifier = if (isEditMode) {
            Modifier.pointerInput(Unit) {
                var localOffset = 0f
                val startIndex = index

                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        localOffset = 0f
                        actions.onDragStart(startIndex)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()      // 또는 consume()
                        localOffset += dragAmount.y
                        actions.onDrag(dragAmount.y)     // ViewModel 갱신(선택)
                    },
                    onDragEnd = {
                        val newIndex = findNewIndex(
                            draggedItemIndex = startIndex,
                            verticalDragOffset = localOffset,   // 최신 오프셋
                            listState = listState,
                            headerItemCount = headerItemCount
                        )
                        if (newIndex != null && newIndex != startIndex) {
                            actions.onReorderComplete(startIndex, newIndex)
                        } else {
                            actions.onReorderCancel()
                        }
                    },
                    onDragCancel = { actions.onReorderCancel() }
                )
            }
        } else Modifier


        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .zIndex(if (isDragging) 1f else 0f)
                .graphicsLayer {
                    translationY = offset
                    shadowElevation = if (isDragging) 8f else 0f
                }
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp, color = Color.Black.copy(alpha = 0.5f)
            )
            RoutineStepItem(
                stepNumber = index + 1,
                step = item,
                isEditMode = isEditMode,
                onDeleteClick = { actions.onDeleteStep(index) },
                onNameChange = { newName -> actions.onStepNameChange(index, newName) },
                dragHandleModifier = dragHandleModifier,
                onTimeClick = { actions.onTimeClick(index) } // [추가]
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp, color = Color.Black.copy(alpha = 0.5f)
            )
        }
    }

    // 스텝 추가 버튼
    if (isEditMode) {
        item("step_add_button") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = actions.onAddStep) {
                    Box(
                        modifier = Modifier
                            .size(29.dp)
                            .background(
                                color = MORUTheme.colors.lightGray,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "STEP 추가",
                            tint = MORUTheme.colors.mediumGray,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Step List - 수정 모드")
@Composable
private fun RoutineStepEditableListPreview_EditMode() {
    MORUTheme {
        val dummySteps = listOf(
            RoutineStep(name = "기상 및 스트레칭", duration = "00:10"),
            RoutineStep(name = "물 한 잔 마시기", duration = "00:01"),
            RoutineStep(name = "책 읽기", duration = "00:30")
        )
        val dummyActions = RoutineStepActions(
            onDragStart = {},
            onDrag = {},
            onDeleteStep = {},
            onStepNameChange = { _, _ -> },
            onReorderCancel = {},
            onAddStep = {},
            onReorderComplete = { _, _ -> },
            onTimeClick = {} // [추가]
        )
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            routineStepEditableList(
                steps = dummySteps,
                isEditMode = true,
                listState = listState,
                draggedStepIndex = null,
                draggedStepVerticalOffset = 0f,
                headerItemCount = 0,
                actions = dummyActions
            )
        }
    }
}

@Preview(showBackground = true, name = "Step List - 보기 모드")
@Composable
private fun RoutineStepEditableListPreview_ViewMode() {
    MORUTheme {
        val dummySteps = listOf(
            RoutineStep(name = "기상 및 스트레칭", duration = "00:10"),
            RoutineStep(name = "물 한 잔 마시기", duration = "00:01"),
            RoutineStep(name = "책 읽기", duration = "00:30")
        )
        val dummyActions = RoutineStepActions(
            onDragStart = {},
            onDrag = {},
            onDeleteStep = {},
            onStepNameChange = { _, _ -> },
            onReorderCancel = {},
            onAddStep = {},
            onReorderComplete = { _, _ -> },
            onTimeClick = {} // [추가]
        )
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            routineStepEditableList(
                steps = dummySteps,
                isEditMode = false,
                listState = listState,
                draggedStepIndex = null,
                draggedStepVerticalOffset = 0f,
                headerItemCount = 0,
                actions = dummyActions
            )
        }
    }
}


