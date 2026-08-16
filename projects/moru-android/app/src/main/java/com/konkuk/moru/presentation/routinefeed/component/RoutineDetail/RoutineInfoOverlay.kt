package com.konkuk.moru.presentation.routinefeed.component.RoutineDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.konkuk.moru.R
import com.konkuk.moru.core.component.chip.MoruChip
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.ui.theme.MORUTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoutineInfoOverlay(
    modifier: Modifier = Modifier,
    routine: Routine,
    onProfileClick: (authorId: String) -> Unit
) {
    //val contentColor = Color.White

    val displayTitle = if (routine.title.length > 8) {
        "${routine.title.take(8)}..."
    } else {
        routine.title
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(enabled = routine.authorId.isNotBlank()) { // 👈 이 부분!
                    onProfileClick(routine.authorId)}
            ) {
                AsyncImage(
                    model = routine.authorProfileUrl,
                    contentDescription = "작성자 프로필",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.ic_profile_with_background),
                    error = painterResource(id = R.drawable.ic_profile_with_background)
                )
                Text(
                    text = routine.authorName,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = displayTitle,
                        color = Color(0xFF595959),
                        style = MORUTheme.typography.title_B_24,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    MoruChip(
                        text = routine.category,
                        onClick = {},
                        isSelected = true,
                        selectedBackgroundColor = Color(0xFFD9F7A2),
                        selectedContentColor = Color(0xFF8CCD00),
                        unselectedBackgroundColor = Color.Transparent,
                        unselectedContentColor = Color.Transparent
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        routine.tags.forEach { tag ->
                            MoruChip(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .height(19.dp),
                                text = "#$tag",
                                onClick = {},
                                isSelected = true,
                                selectedBackgroundColor = MORUTheme.colors.darkGray,
                                selectedContentColor = MORUTheme.colors.limeGreen,
                                unselectedBackgroundColor = Color.Transparent,
                                unselectedContentColor = Color.Transparent,
                                contentPadding = PaddingValues(
                                    horizontal = 5.dp,
                                    vertical = 1.4.dp
                                )
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = routine.description,
            color = Color(0xFF000000),
            style = MORUTheme.typography.time_R_14,
            modifier = Modifier.fillMaxWidth()
        )
    }
}