package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun RoutineResultRow(
    icon: Int,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "아이콘",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(9.9.dp))
            Text(
                text = label,
                style = typography.body_SB_14,
                color = colors.mediumGray
            )
        }
        Text(
            text = value,
            style = typography.body_SB_14,
            color = colors.black
        )
    }
}

@Preview()
@Composable
private fun RoutineResultRowPreview() {
    RoutineResultRow(
        icon = R.drawable.schedule_icon,
        label = "루틴",
        value = "주말 아침 루틴"
    )
}