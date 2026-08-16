package com.konkuk.moru.core.component.routinedetail.appdisplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun UnselectedAppInBottomSheet(
    appIcon: ImageBitmap = ImageBitmap(64, 64),
    appName: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            bitmap = appIcon,
            contentDescription = "$appName icon",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color.White)
        )
        Text(
            text = appName,
            style = typography.desc_M_12,
            color = colors.mediumGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnselectedAppPreview() {
    val dummyBitmap = createBitmap(64, 64).apply {
        eraseColor(0xFFFFFFFF.toInt()) // 회색 배경
    }
    val dummyImageBitmap = dummyBitmap.asImageBitmap()

    UnselectedAppInBottomSheet(
        appIcon = dummyImageBitmap,
        appName = "Sample App"
    ){}
}