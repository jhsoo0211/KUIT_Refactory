package com.konkuk.moru.core.component


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
fun SwitchWithCustomColors() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MORUTheme.colors.mediumGray ,
            checkedTrackColor = MORUTheme.colors.lightGray,
            uncheckedThumbColor = MORUTheme.colors.mediumGray,
            uncheckedTrackColor = Color.Black,
        )
    )
}

@Composable
@Preview(showBackground = true)
fun SwitchScreen(){
    SwitchWithCustomColors()
}
