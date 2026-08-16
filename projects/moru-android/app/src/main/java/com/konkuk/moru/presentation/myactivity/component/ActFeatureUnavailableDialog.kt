package com.konkuk.moru.presentation.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors

@Composable
fun MyActFeatureUnavailableDialog(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.paleLime,
        titleContentColor = colors.black,
        textContentColor = colors.black,
        shape = RoundedCornerShape(16.dp),
        title = { Text("알림") },
        text = { Text("현재 버전에서 제공되지 않는 기능입니다.") },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.oliveGreen,
                    contentColor = Color.White
                )
            ) {
                Text("확인")
            }
        }
    )
}

