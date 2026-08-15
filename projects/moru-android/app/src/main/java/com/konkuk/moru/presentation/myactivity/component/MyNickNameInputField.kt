package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun MyActNickNameInputField(
    value: String,                         // ← VM이 들고 있는 닉네임
    onValueChange: (String) -> Unit,       // ← VM setter
    status: MyActNicknameStatus,           // ← VM이 계산한 상태
    onClickCheck: () -> Unit,              // ← 중복확인 액션
    placeholder: String = "닉네임을 입력하세요",
    modifier: Modifier = Modifier
) {

    val borderColor = when (status) {
        MyActNicknameStatus.NONE, MyActNicknameStatus.FOCUS, MyActNicknameStatus.VALID -> colors.limeGreen
        MyActNicknameStatus.ERROR -> colors.red
    }
    val helperText = when (status) {
        MyActNicknameStatus.VALID -> "사용 가능한 닉네임입니다."
        MyActNicknameStatus.ERROR -> "이미 사용 중인 닉네임입니다."
        else -> ""
    }
    val helperColor = when (status) {
        MyActNicknameStatus.VALID -> colors.limeGreen
        MyActNicknameStatus.ERROR -> colors.red
        else -> Color.Transparent
    }
    val buttonColor = when (status) {
        MyActNicknameStatus.NONE -> colors.veryLightGray
        MyActNicknameStatus.VALID -> colors.paleLime
        MyActNicknameStatus.ERROR -> colors.red
        else -> colors.veryLightGray
    }
    val buttonTextColor = when (status) {
        MyActNicknameStatus.NONE -> colors.mediumGray
        MyActNicknameStatus.VALID -> colors.oliveGreen
        MyActNicknameStatus.ERROR -> Color(0xFFFFFFFF)
        else -> colors.mediumGray
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text("닉네임", color = colors.black, style = typography.body_SB_16, modifier = Modifier.padding(bottom = 6.dp))
            if (helperText.isNotEmpty()) {
                Text(helperText, color = helperColor, style = typography.desc_M_14)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                .padding(end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,                  // ← 외부로 전달
                    singleLine = true,
                    textStyle = typography.desc_M_14.copy(color = colors.black),
                    modifier = Modifier.weight(1f).height(45.dp),
                    decorationBox = { inner ->
                        Box(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(placeholder, style = typography.desc_M_14, color = colors.mediumGray)
                            }
                            inner()
                        }
                    }
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(56.dp)
                        .height(21.dp)
                        .background(buttonColor, RoundedCornerShape(10.5.dp))
                        .clickable { onClickCheck() }  // ← VM에서 중복확인
                ) {
                    Text("중복확인", color = buttonTextColor, style = typography.desc_M_12)
                }
            }
        }
    }
}
