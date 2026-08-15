package com.konkuk.moru.presentation.onboarding.component

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun BirthdayField(
    birthday: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .border(
                1.dp,
                color = if (birthday.isNotEmpty()) colors.limeGreen else colors.lightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(start = 16.dp, end = 14.dp)
            .pointerInput(Unit) {
                detectTapGestures { onClick() }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = if (birthday.isNotEmpty()) birthday else "생년월일을 선택하세요",
            style = typography.desc_M_14,
            color = colors.mediumGray,
            modifier = Modifier
                .align(Alignment.CenterStart)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_onboarding_calender),
            contentDescription = "Calendar Icon",
            tint = colors.mediumGray,
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )
    }
}

@Preview
@Composable
private fun BirthdayFieldPreview() {
    val id = "1990-01-01"
    BirthdayField(
        birthday = id,
        onClick = {}
    )
}