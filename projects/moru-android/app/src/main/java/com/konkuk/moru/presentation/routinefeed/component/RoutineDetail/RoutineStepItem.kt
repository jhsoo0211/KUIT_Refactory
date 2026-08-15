package com.konkuk.moru.presentation.routinefeed.component.RoutineDetail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.data.model.RoutineStep
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
fun RoutineStepItem(stepNumber: Int, step: RoutineStep, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(16.dp))
        Text(text = "$stepNumber", style = MORUTheme.typography.body_SB_14, color = Color.Gray)
        Spacer(Modifier.width(41.dp))
        Text(
            text = step.name,
            style = MORUTheme.typography.body_SB_14,
            modifier = Modifier.weight(1f)
        )
        Text(text = step.duration, style = MORUTheme.typography.body_SB_14)
        Spacer(Modifier.width(12.dp))
    }
}


@Preview(showBackground = true, name = "Routine Step Item")
@Composable
private fun RoutineStepItemPreview() {
    val sampleStep = RoutineStep(name = "아침 스트레칭", duration = "10:00")

    MORUTheme {
        RoutineStepItem(
            stepNumber = 1,
            step = sampleStep
        )
    }
}