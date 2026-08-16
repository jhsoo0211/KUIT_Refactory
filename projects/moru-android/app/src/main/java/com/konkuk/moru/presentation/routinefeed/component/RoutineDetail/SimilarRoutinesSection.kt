package com.konkuk.moru.presentation.routinefeed.component.RoutineDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.konkuk.moru.data.model.SimilarRoutine
import com.konkuk.moru.ui.theme.MORUTheme


@Composable
fun SimilarRoutinesSection(
    modifier: Modifier = Modifier,
    routines: List<SimilarRoutine>,
    onRoutineClick: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MORUTheme.colors.veryLightGray)
            .padding(vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "이 루틴과 비슷한 루틴",
                style = MORUTheme.typography.title_B_20,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "더보기",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        LazyRow(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 80.dp,
                top = 0.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(routines) { routine ->
                SimilarRoutineCard(
                    routine = routine,
                    onClick = { onRoutineClick(routine.id) })
            }
        }
    }
}
