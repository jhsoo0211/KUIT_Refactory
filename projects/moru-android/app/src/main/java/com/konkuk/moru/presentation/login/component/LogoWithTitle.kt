package com.konkuk.moru.presentation.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R

@Composable
fun LogoWithTitle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_login_moru_logo),
            contentDescription = "Moru Logo",
            modifier = Modifier
                .padding(0.dp)
                .width(110.dp)
                .height(139.45413.dp),
        )
        //Spacer(modifier = Modifier.height(12.55.dp))
        Text(
            text = "MORU",
            style = TextStyle(
                fontSize = 24.sp,
                //fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),
                lineHeight = 24.sp
            )
        )
        Text(
            text = "모두의 루틴",
            style = TextStyle(
                fontSize = 20.sp,
                //fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),
                lineHeight = 20.sp
            )
        )
    }

}

@Preview(showBackground = true, backgroundColor = 0xFF212120)
@Composable
private fun LogoWithTitlePreview() {
    LogoWithTitle()
}