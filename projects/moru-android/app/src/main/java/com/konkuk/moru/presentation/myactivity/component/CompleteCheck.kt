package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun CompleteCheck(
    complete: Boolean = false,
    modifier: Modifier = Modifier
) {
    if( complete ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(colors.black, shape = RoundedCornerShape(size = 100.dp))
                .width(32.dp)
                .height(17.dp)

        ) {
            Text(
                text = "완료",
                color = Color(0xFFFFFFFF),
                style = typography.desc_M_12,
                modifier = Modifier
            )
        }
    }else{
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(size = 100.dp))
                .border(
                    width = 1.dp,
                    color = colors.black,
                    shape = RoundedCornerShape(size = 100.dp)
                )
                .width(40.dp)
                .height(18.dp)
        ) {
            Text(
                text = "미완료",
                color = colors.black,
                style = typography.desc_M_12,
                modifier = Modifier
            )
        }
    }
}