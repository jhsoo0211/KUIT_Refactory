package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinePaceInfo(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onDetailClick: () -> Unit,
    renewalDate: String = "2025.01.01",
    progress: Float = 0.1f,
    modifier: Modifier = Modifier
) {
    val (badgeRes, titleText, desText) = when (progress) {
        in 0f..0.3f -> Triple(R.drawable.ic_third_badge, "잠시 걷는 중", "모든 루틴을 지키는건 쉽지않아요.\n괜찮아요. 모두에게는 숨을 고르는\n순간이 필요하니까요.")
        in 0.3f..0.7f -> Triple(R.drawable.ic_second_badge, "간헐적 루틴러", "완벽하지 않아도,\n끊기지 않는 실천이 중요해요.\n천천히 페이스를 올려봐요!")
        else -> Triple(R.drawable.ic_first_badge, "루틴 페이스 메이커", "꾸준한 실천으로 다른 사용자들에게\n흐름을 만들어주고 있어요!\n당신 덕분에 누군가는 루틴을 시작했을지도 몰라요.")
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dragHandle = null,
        scrimColor = colors.black.copy(alpha = 0.5f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(30.dp)
                    .height(5.dp)
                    .background(
                        color = colors.lightGray,
                        RoundedCornerShape(100.dp)
                    )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "루틴 페이스",
                    style = typography.body_SB_16
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            val scrollState = rememberScrollState()
            Column(
                modifier = modifier
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "루틴 페이스는 일주일간의 실천 데이터를 바탕으로,\n당신의 루틴 실천 리듬을 분석 합니다.\n" +
                            "매주 월요일, 지금 나의 루틴 페이스는 어떤지 알 수 있어요.",
                    style = typography.time_R_12.copy(
                        lineHeight = 20.sp
                    ),
                    color = colors.mediumGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(279.dp)
                        .background(color = colors.black, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Image(
                            painter = painterResource(id = badgeRes),
                            contentDescription = titleText,
                            modifier = Modifier.size(160.dp)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = titleText,
                            color = colors.limeGreen,
                            style = typography.body_SB_16,
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = desText,
                            style = typography.desc_M_12.copy(lineHeight = 14.sp),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("최근 갱신 일자: ", style = typography.time_R_12, color = colors.mediumGray)
                Text(
                    text = renewalDate,
                    style = typography.time_R_12,
                    color = colors.mediumGray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onDetailClick,
                colors = ButtonDefaults.buttonColors(containerColor = colors.limeGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "자세히 보기",
                    color = colors.black,
                    style = typography.body_SB_16,
                )
            }
        }
    }
}
