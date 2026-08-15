package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R

@Composable
fun BottomOverlayBarItem(
    iconResId: Int,
    label: String,
    iconWidth: Dp = 16.dp,
    iconHeight: Dp = 17.5.dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(70.dp)
            .height(52.dp),  // Material3 기준
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier
                .width(iconWidth)
                .height(iconHeight),
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
                color = Color.White
            )
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 360,
    heightDp = 100
)
@Composable
private fun BottomOverlayBarItemPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
        ) {
            // 첫번째 빈 칸
            Box(modifier = Modifier.weight(1f))

            // 두번째: 루틴 피드
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                BottomOverlayBarItem(R.drawable.ic_routine_feed_white, "루틴 피드")
            }

            // 세번째: 내 루틴
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                BottomOverlayBarItem(R.drawable.ic_my_routine_white, "내 루틴")
            }

            // 네번째: 내 활동
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                BottomOverlayBarItem(R.drawable.ic_my_activity_white, "내 활동")
            }
        }
    }
}