package com.konkuk.moru.presentation.routinefeed.component.RoutineDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.button.MoruButton
import com.konkuk.moru.data.model.DummyData
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.ui.theme.MORUTheme


@Composable
fun RoutineStepSection(
    modifier: Modifier = Modifier,
    routine: Routine,
    isAdding: Boolean,
    showAddButton: Boolean,
    onAddToMyRoutineClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("STEP", style = MORUTheme.typography.title_B_20, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(16.dp))
            if (showAddButton) {
                MoruButton(
                    text = ("내 루틴에 추가"),
                    enabled = !isAdding,
                    onClick = onAddToMyRoutineClick,
                    backgroundColor = MORUTheme.colors.limeGreen,
                    contentColor = Color.White,
                    textStyle = MORUTheme.typography.body_SB_14,
                    iconContent = { Icon(Icons.Default.CalendarToday, "캘린더", Modifier.size(16.dp)) }
                )

            }
        }
        Spacer(Modifier.height(16.dp))

        Column {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black.copy(alpha = 0.5f)
            )
            routine.steps.forEachIndexed { index, step ->
                RoutineStepItem(
                    stepNumber = index + 1,
                    step = step,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (index < routine.steps.lastIndex) {
                    Column {
                        HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.5f))
                    }
                } else {
                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Step Section (with Add Button)")
@Composable
private fun RoutineStepSectionWithButtonPreview() {
    // DummyData에서 '내 루틴' 중 하나를 가져와서 사용
    val sampleRoutine = DummyData.feedRoutines.find { it.authorId == DummyData.MY_USER_ID }!!

    MORUTheme {
        RoutineStepSection(
            routine = sampleRoutine,
            showAddButton = true,
            onAddToMyRoutineClick = {},
            isAdding = true,
        )
    }
}
