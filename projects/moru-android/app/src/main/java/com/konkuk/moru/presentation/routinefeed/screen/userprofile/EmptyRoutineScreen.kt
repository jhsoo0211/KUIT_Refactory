package com.konkuk.moru.presentation.routinefeed.screen.userprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
fun EmptyRoutineView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_empty_routine_box), // ic_empty_routine_box.xml 아이콘 리소스가 필요합니다.
            contentDescription = "루틴 없음",
            modifier = Modifier.size(81.dp),
            tint = MORUTheme.colors.darkGray
        )
        Spacer(modifier = Modifier.height(37.dp))
        Text(
            text = "아직 내 루틴이 비어있어요.",
            style = MORUTheme.typography.desc_M_20 ,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = "당신만의 루틴을 직접 만들거나,\n다른 사람의 루틴을 참고해보세요!",
            style = MORUTheme.typography.desc_M_16 ,
            color = MORUTheme.colors.darkGray,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun EmptyRoutineViewPreview() {
    MORUTheme {
        EmptyRoutineView()
    }
}