package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors


@Composable
fun PageIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color = colors.veryLightGray, shape = RoundedCornerShape(7.dp))
            .width(58.dp)
            .height(12.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { index ->
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .height(6.dp)
                        .background(
                            color = if (index == currentPage) colors.black else colors.lightGray,
                            shape = RoundedCornerShape(percent = 100)
                        )
                )
                if (index < pageCount - 1) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
    }
}