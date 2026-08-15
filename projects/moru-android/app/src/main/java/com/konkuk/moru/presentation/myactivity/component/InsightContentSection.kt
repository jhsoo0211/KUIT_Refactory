package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.konkuk.moru.presentation.myactivity.viewmodel.InsightUiState
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun InsightContentSection(
    insightScore: Int?,
    insightData: InsightUiState,
    userName: String = "사용자명",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (insightScore != null) {
            Spacer(modifier = Modifier.height(32.dp))
            InsightGraph(insightData, userName)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 107.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "아직 실천 기록이 부족해요.",
                    style = typography.desc_M_20,
                    color = colors.black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "루틴을 꾸준히 실천하여 나만의\n인사이트를 받아보세요!",
                    style = typography.desc_M_14,
                    color = colors.mediumGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}