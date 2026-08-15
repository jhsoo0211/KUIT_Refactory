package com.konkuk.moru.core.component.routinedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.data.model.DummyData
import com.konkuk.moru.data.model.RoutineStep
import com.konkuk.moru.ui.theme.MORUTheme


@Composable
fun RoutineStepItem(
    stepNumber: Int,
    step: RoutineStep,
    isEditMode: Boolean,
    onDeleteClick: () -> Unit,
    onNameChange: (String) -> Unit,
    dragHandleModifier: Modifier = Modifier, // 드래그 핸들 Modifier를 파라미터로 받음
    onTimeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isEditMode) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Drag Handle",
                // 부모로부터 전달받은 Modifier를 여기에 적용
                modifier = Modifier
                    .size(24.dp)
                    .then(dragHandleModifier),
                tint = MORUTheme.colors.lightGray
            )
        } else {
            Text(
                text = "%2d".format(stepNumber),
                style = MORUTheme.typography.body_SB_14,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.width(24.dp))

        if (isEditMode) {
            // 수정 모드일 때: BasicTextField를 보여줌
            BasicTextField(
                value = step.name,
                onValueChange = onNameChange, // 타이핑할 때마다 onNameChange 콜백 호출
                modifier = Modifier.weight(1f),
                textStyle = MORUTheme.typography.body_SB_14.copy(color = Color(0xFF1A1A1A)),
                singleLine = true // 한 줄 입력 필드로 설정
            )
        } else {
            // 보기 모드일 때: 기존처럼 Text를 보여줌
            Text(
                text = step.name,
                style = MORUTheme.typography.body_SB_14,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.weight(1f),
            )
        }

        Text(
            text = step.duration,
            style = MORUTheme.typography.body_SB_14,
            color = MORUTheme.colors.darkGray,
            modifier = if (isEditMode) Modifier.clickable { onTimeClick() } else Modifier // [추가]
        )

        if (isEditMode) {
            Spacer(Modifier.width(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_delete_gray),
                contentDescription = "Delete Step",
                modifier = Modifier
                    .size(14.dp)
                    .clickable(onClick = onDeleteClick),
                tint = MORUTheme.colors.mediumGray
            )
        }
    }
}

@Preview(showBackground = true, name = "스텝 아이템 - 보기 모드")
@Composable
private fun LocalRoutineStepItemPreview_ViewMode() {
    // DummyData에서 루틴의 첫 번째 스텝을 가져옵니다.
    val sampleStep = DummyData.feedRoutines.first().steps.first()
    MORUTheme {
        RoutineStepItem(
            stepNumber = 1,
            step = sampleStep,
            isEditMode = false,
            onDeleteClick = {},
            onNameChange = {},
            dragHandleModifier = Modifier,
            onTimeClick = {},
        )
    }
}

@Preview(showBackground = true, name = "스텝 아이템 - 수정 모드")
@Composable
private fun LocalRoutineStepItemPreview_EditMode() {
    // DummyData에서 루틴의 첫 번째 스텝을 가져옵니다.
    val sampleStep = DummyData.feedRoutines.first().steps.first()
    MORUTheme {
        RoutineStepItem(
            stepNumber = 1,
            step = sampleStep,
            isEditMode = true,
            onDeleteClick = {},
            onTimeClick = {},
            dragHandleModifier = Modifier,
            onNameChange = {}
        )
    }
}