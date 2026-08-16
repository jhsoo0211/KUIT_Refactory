package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun MyActSelfIntroductionField(
    value: String,                       // ← ViewModel이 가진 값
    onValueChange: (String) -> Unit,     // ← ViewModel setter
    maxLength: Int = 20,
    placeholder: String = "자기소개를 입력하세요",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "자기소개",
            style = typography.body_SB_16,
            color = colors.black,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(6.dp))
        ) {
            BasicTextField(
                value = value,
                onValueChange = { s -> onValueChange(s.take(maxLength)) }, // 길이 제한
                textStyle = typography.desc_M_14.copy(color = colors.black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = typography.desc_M_14,
                            color = Color.Gray
                        )
                    }
                    inner()
                }
            )
        }

        // (선택) 글자수 표시
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${value.length}/$maxLength",
                style = typography.desc_M_12,
                color = colors.mediumGray
            )
        }
    }
}
