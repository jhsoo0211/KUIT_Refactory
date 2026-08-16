package com.konkuk.moru.core.component.Switch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.typography
import com.konkuk.moru.R

@Composable
fun StatusBarMock(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {

    //베경색
    val backgroundColor = if (isDarkMode) Color.Black else Color.White

    //텍스트와 아이콘 색
    val contentColor = if (isDarkMode) Color.White else Color.Black

    Row(
        modifier = modifier
            .width(360.dp)
            .height(24.dp)
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 왼쪽 시간
        Text(
            text = "12:30",
            color = contentColor,
            style = typography.desc_M_14
        )

        // 오른쪽 아이콘들
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_signal),
                contentDescription = "신호",
                colorFilter = ColorFilter.tint(contentColor)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_wifi),
                contentDescription = "와이파이",
                colorFilter = ColorFilter.tint(contentColor)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_battery),
                contentDescription = "배터리",
                colorFilter = ColorFilter.tint(contentColor)
            )
        }
    }
}

//화이트모드
@Preview
@Composable
private fun StatusBarMockPreview() {
    StatusBarMock(isDarkMode = false)
}

//다크모드
@Preview
@Composable
private fun StatusBarMockDarkPreview() {
    StatusBarMock(isDarkMode = true)
}
