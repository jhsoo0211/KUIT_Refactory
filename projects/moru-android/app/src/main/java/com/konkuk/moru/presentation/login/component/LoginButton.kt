package com.konkuk.moru.presentation.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun LoginButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if(enabled) colors.limeGreen else colors.lightGray
    // 이 버튼 클릭 물결 제거해야함
//    Button(
//        onClick = onClick,
//        //enabled = enabled,
//        shape = RoundedCornerShape(4.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = backgroundColor,
//            contentColor = Color(0xFF212120)
//        ),
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(46.dp)
//    ) {
//        Text("로그인", style = typography.body_SB_16)
//    }
    // 똑같이 생겼지만 Box 로 만든 버튼
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clickable(
                indication = null,
                interactionSource = null
            ){ onClick() }
            .background(backgroundColor, shape = RoundedCornerShape(4.dp)),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text("로그인", style = typography.body_SB_16, color = Color(0xFF212120))
    }

}

@Preview
@Composable
private fun LoginButtonPreview() {
    LoginButton(false){}
}