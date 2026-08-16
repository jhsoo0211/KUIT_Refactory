package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.konkuk.moru.core.util.modifier.noRippleClickable
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import com.konkuk.moru.R
import com.konkuk.moru.presentation.myactivity.viewmodel.InsightUiState
import com.konkuk.moru.presentation.navigation.Route

@Composable
fun MyActivityTab(
    insightData: InsightUiState,
    userName: String = "사용자명",
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val tabTitles = listOf("내 정보", "인사이트")

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabTitles.forEachIndexed { index, title ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable { onTabSelected(index) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = typography.body_SB_14,
                        color = if (index == selectedTab) colors.black else colors.mediumGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(100.dp)
                            .background(
                                if (index == selectedTab) colors.black else Color.Transparent
                            )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.veryLightGray)
        )
    }

    when (selectedTab) {
        0 -> {
            Spacer(modifier = Modifier.height(40.dp))
            MyInfoDetail(myInfoDetailIcon = R.drawable.ic_heart_a, title = "내 관심 태그", onClick = {
                navController.navigate(
                    Route.ActFabTag.route
                )
            })
            Spacer(modifier = Modifier.height(6.dp))
            MyInfoDetail(myInfoDetailIcon = R.drawable.ic_graph_a, title = "내 기록", onClick = {
                navController.navigate(
                    Route.ActRecord.route
                )
            })
            Spacer(modifier = Modifier.height(6.dp))
            MyInfoDetail(myInfoDetailIcon = R.drawable.ic_scrap_a, title = "스크랩한 루틴", onClick = {
                navController.navigate(
                    Route.ActScrab.route
                )
            })
        }

        1 -> {
            InsightContentSection((insightData.routineCompletionRate*100).toInt(),insightData, userName)
        }
    }
}
