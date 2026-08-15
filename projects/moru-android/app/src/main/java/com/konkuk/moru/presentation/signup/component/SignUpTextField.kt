package com.konkuk.moru.presentation.signup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor =
        if (isValid) {
            if (isFocused) colors.limeGreen else colors.lightGray
        } else colors.red

    val isPasswordField =
        keyboardType == KeyboardType.Password || visualTransformation == PasswordVisualTransformation()
    var isPasswordVisible by remember { mutableStateOf(false) }
    val effectiveVisualTransformation =
        if (isPasswordField && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = effectiveVisualTransformation,
        interactionSource = interactionSource,
        textStyle = typography.desc_M_14,
        cursorBrush = SolidColor(colors.limeGreen),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
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
                        color = if (isFocused) colors.darkGray else colors.mediumGray,
                    )
                }
                innerTextField()
                if (isPasswordField) {
                    Icon(
                        imageVector = if (isPasswordVisible)
                            Icons.Outlined.Visibility
                        else
                            Icons.Outlined.VisibilityOff,
                        contentDescription = "Toggle Password Visibility",
                        tint = colors.mediumGray,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { isPasswordVisible = !isPasswordVisible }
                            .align(Alignment.CenterEnd)
                            .size(20.dp),
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun BasicPreview() {
    Column {
        SignUpTextField(
            value = "",
            onValueChange = {},
            isValid = true,
            placeholder = "이메일",
            keyboardType = KeyboardType.Email,
            visualTransformation = VisualTransformation.None,
        )
        Spacer(modifier = Modifier.height(10.dp))
        SignUpTextField(
            value = "",
            onValueChange = {},
            isValid = true,
            placeholder = "비밀번호",
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
        )
    }

}