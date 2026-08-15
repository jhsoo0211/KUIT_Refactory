package com.konkuk.moru.presentation.routinecreate.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

/**
 * 순수 UI용 Step 아이템
 *
 * @param title            현재 스텝 제목
 * @param timeDisplay      "HH:mm:ss" 또는 "" (빈 문자열이면 '소요 시간' 플레이스홀더 표시)
 * @param isFocusingRoutine true면 시간 필드 노출
 * @param stepCount        리스트 총 개수(1개일 땐 삭제 아이콘 숨김)
 * @param onTitleChange    제목 입력 변경 콜백
 * @param onShowTimePicker 시간 클릭 시 호출(외부에서 다이얼 열기)
 * @param onDelete         삭제 아이콘 클릭 시 호출
 */
@Composable
fun StepItem(
    title: String,
    timeDisplay: String,
    isFocusingRoutine: Boolean,
    stepCount: Int,
    onTitleChange: (String) -> Unit,
    onShowTimePicker: () -> Unit,
    onDelete: () -> Unit
) {
    var titleInput by remember(title) { mutableStateOf(title) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(57.dp)
    ) {
        HorizontalDivider(thickness = 1.dp, color = colors.mediumGray)

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_steplist_burger),
                contentDescription = "Step List Icon",
                modifier = Modifier.size(24.dp),
                tint = colors.lightGray
            )
            Spacer(modifier = Modifier.width(21.dp))

            // 활동명
            BasicTextField(
                value = titleInput,
                onValueChange = {
                    titleInput = it
                    onTitleChange(it)
                },
                singleLine = true,
                textStyle = typography.body_SB_14.copy(color = colors.charcoalBlack),
                modifier = Modifier
                    .weight(0.34f)
                    .padding(end = 4.dp),
                decorationBox = { innerTextField ->
                    if (titleInput.isEmpty()) {
                        Text(
                            text = "활동명",
                            style = typography.body_SB_14,
                            color = colors.charcoalBlack,
                        )
                    }
                    innerTextField()
                }
            )

            // 소요 시간 (포커싱 루틴일 때만 노출)
            if (isFocusingRoutine) {
                Text(
                    text = if (timeDisplay.isBlank()) "소요 시간" else timeDisplay,
                    style = typography.body_SB_14,
                    color = colors.charcoalBlack,
                    modifier = Modifier
                        .weight(0.32f)
                        .clickable { onShowTimePicker() }
                )
            }

            // 삭제 아이콘 (스텝이 2개 이상일 때만)
            if (stepCount > 1) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "Step 삭제",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onDelete() },
                    tint = colors.mediumGray
                )
            } else {
                Spacer(modifier = Modifier.width(26.dp))
            }
        }

        HorizontalDivider(thickness = 1.dp, color = colors.mediumGray)
    }
}

@Preview
@Composable
private fun StepItemPreview_Focusing() {
    StepItem(
        title = "스트레칭",
        timeDisplay = "00:05:00",
        isFocusingRoutine = true,
        stepCount = 2,
        onTitleChange = {},
        onShowTimePicker = {},
        onDelete = {}
    )
}

@Preview
@Composable
private fun StepItemPreview_Simple() {
    StepItem(
        title = "",
        timeDisplay = "",
        isFocusingRoutine = false,
        stepCount = 1,
        onTitleChange = {},
        onShowTimePicker = {},
        onDelete = {}
    )
}