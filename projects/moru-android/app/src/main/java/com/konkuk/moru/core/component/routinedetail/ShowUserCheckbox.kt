package com.konkuk.moru.core.component.routinedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun ShowUserCheckbox(
    showUser: Boolean,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = null
            ) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = if (showUser) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_uncheck),
            contentDescription = "사용자 표시 아이콘",
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(3.5.dp))
        Text("사용자 표시", style = typography.time_R_12)
    }
}

@Preview
@Composable
private fun ShowUserCheckboxPreview() {
    var showUser by remember { mutableStateOf(true) }
    ShowUserCheckbox(
        showUser = showUser,
        onClick = {
            showUser = !showUser
        }
    )
}