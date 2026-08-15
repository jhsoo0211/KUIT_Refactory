package com.konkuk.moru.presentation.myactivity.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.core.util.modifier.noRippleClickable
import com.konkuk.moru.presentation.common.MyActFeatureUnavailableDialog
import com.konkuk.moru.presentation.myactivity.component.BackTitle
import com.konkuk.moru.presentation.myactivity.viewmodel.InsightViewModel
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun ActSettingScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var showPopup by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(colors.veryLightGray)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        BackTitle(title = "설정", navController)
        Spacer(modifier = Modifier.padding(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(4.dp))
        ) {
            val settings = listOf(
                "좋아요한 루틴" to {showPopup = true},
                "알림 설정" to {showPopup = true},
                "개인정보처리방침" to {navController.navigate(Route.ActPolicy.route)},
                "로그 아웃" to {},
                "탈퇴" to {}
            )

            settings.forEachIndexed { index, (title, action) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .padding(start = 16.dp, end = 16.dp)
                        .clickable { action() }
                ) {
                    Text(
                        text = title,
                        color = if (title == "탈퇴") colors.red else colors.black,
                        style = typography.desc_M_16,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painterResource(R.drawable.ic_arrow_b),
                        contentDescription = "arrow icon",
                        tint = colors.mediumGray,
                        modifier = Modifier
                            .width(6.46.dp)
                            .height(14.dp)
                    )
                }
                if (title != "탈퇴") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(start = 16.dp, end = 16.dp)
                            .background(colors.lightGray)
                    ) {}
                }
            }
        }
    }

    MyActFeatureUnavailableDialog(
        visible = showPopup,
        onDismiss = { showPopup = false }
    )
}