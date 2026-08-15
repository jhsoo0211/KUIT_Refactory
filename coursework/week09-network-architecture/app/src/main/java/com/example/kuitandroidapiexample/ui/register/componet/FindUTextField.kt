package com.example.kuitandroidapiexample.ui.register.componet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.colors
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.typography

@Composable
fun FindUTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = title,
            style = typography.semiBold.copy(fontSize = 18.sp)
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = colors.gray100,
                    shape = RoundedCornerShape(8.dp)
                ),
            value = value,
            onValueChange = onValueChanged,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colors.gray200,
                unfocusedContainerColor = colors.gray200,
                focusedIndicatorColor = Color.Unspecified,
                unfocusedIndicatorColor = Color.Unspecified
            ),
            textStyle = typography.regular.copy(fontSize = 14.sp, color = colors.gray100)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun FindUTextFieldPreview() {
    var value by remember { mutableStateOf("조익성") }

    FindUTextField(
        title = "이름 입력",
        value = value,
    ) { value = it }
}