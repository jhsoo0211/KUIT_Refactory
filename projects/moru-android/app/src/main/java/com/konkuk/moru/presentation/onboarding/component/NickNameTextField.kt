package com.konkuk.moru.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.presentation.onboarding.model.NickNameValid
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun NickNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onCheckValidNickname: () -> Unit,
    validity: NickNameValid,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        validity.isValid() -> colors.limeGreen
        validity.isInvalid() -> colors.red   // ✅ 중복 시 빨간색
        isFocused -> colors.mediumGray
        else -> colors.lightGray
    }

    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= 7) {
                onValueChange(newValue)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        textStyle = typography.desc_M_14,
        cursorBrush = SolidColor(colors.limeGreen),
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .background(color = Color.Transparent),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = typography.desc_M_14,
                        color = if (isFocused) colors.darkGray else colors.mediumGray
                    )
                }
                innerTextField()
                Column(
                    modifier = Modifier
                        .background(
                            color = when {
                                validity.isValid() -> colors.paleLime
                                validity.isInvalid() -> colors.red
                                else -> colors.veryLightGray
                            },
                            shape = RoundedCornerShape(10.5.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 4.dp)
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = onCheckValidNickname
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "중복확인",
                        style = typography.desc_M_12,
                        color = when {
                            validity.isValid() -> colors.oliveGreen
                            validity.isInvalid() -> Color.White
                            else -> colors.mediumGray
                        }
                    )
                }

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun BasicPreview() {
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        NickNameTextField(
            value = "",
            onValueChange = {},
            onCheckValidNickname = {},
            validity = NickNameValid.EMPTY,
            placeholder = "닉네임",
            keyboardType = KeyboardType.Email,
            visualTransformation = VisualTransformation.None,
        )
    }

}