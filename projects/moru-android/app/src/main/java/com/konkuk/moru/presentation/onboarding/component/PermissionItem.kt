package com.konkuk.moru.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.presentation.onboarding.model.PermissionType
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun PermissionItem(
    title: String,
    description: String,
    type: PermissionType,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = null
            ) { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = typography.body_SB_16
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_permission_vector),
                    contentDescription = "permission icon"
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = typography.desc_M_14,
                color = colors.mediumGray
            )
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(50))
                .border(width = 1.dp, color = colors.limeGreen, shape = RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            if (isGranted) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color = colors.limeGreen, shape = RoundedCornerShape(50))
                )
            }
        }
    }
}

@Preview
@Composable
private fun PermissionItemPreview() {
    PermissionItem(
        title = "푸시 알림 허용",
        description = "루틴 실천에 도움되는 알림을 받으세요!",
        type = PermissionType.PUSH_NOTIFICATION,
        isGranted = true,
        onClick = {}
    )
}