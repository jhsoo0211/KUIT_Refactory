package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.core.util.modifier.noRippleClickable
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinePaceCard(
    userName: String = "사용자명",
    routinePace: String = "미정",
    progress: Float = 0.1f,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 24.dp)
            .height(126.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = userName,
            style = typography.body_SB_16.copy(fontSize = 20.sp),
            color = colors.black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = routinePace, style = typography.desc_M_16, color = colors.mediumGray)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                painterResource(R.drawable.ic_routinepace_info),
                contentDescription = "routinepace_info",
                tint = colors.mediumGray,
                modifier = Modifier
                    .size(15.dp)
                    .noRippleClickable {
                        isSheetOpen = true
                    }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(42.dp)
                    .weight(1f)
            ) {
                MovingIconProgressBar(progress)
            }

            Spacer(modifier = Modifier.width(5.dp))

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = (progress * 100).roundToInt().toString() + "%",
                    color = colors.limeGreen,
                    style = typography.body_SB_16
                )
            }
        }
        if (isSheetOpen) {
            RoutinePaceInfo(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState,
                onDetailClick = {
                    isSheetOpen = false
                    navController.navigate(Route.ActInsightInfo.route)
                                },
                renewalDate = "2025.07.06",
                progress = progress
            )
        }
    }
}
