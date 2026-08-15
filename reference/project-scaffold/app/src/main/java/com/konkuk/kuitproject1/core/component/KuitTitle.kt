package com.konkuk.kuitproject1.core.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun KuitTitle(
    title: String,
    modifier: Modifier =Modifier
){
    Text(
        text= title,
        fontSize=20.sp
    )

}