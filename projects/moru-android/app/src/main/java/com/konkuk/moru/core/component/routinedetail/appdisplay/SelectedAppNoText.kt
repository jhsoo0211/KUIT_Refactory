package com.konkuk.moru.core.component.routinedetail.appdisplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun SelectedAppNoText(
    appIcon: ImageBitmap = ImageBitmap(64, 64),
    isRemovable: Boolean,
    onRemove: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.clickable(
            onClick = if (isRemovable) onRemove ?: { } else { {} },
            indication = null,
            interactionSource = null
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(53.dp)
        ) {
            Image(
                bitmap = appIcon,
                contentDescription = "app icon",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color.White)
                    .align(Alignment.BottomEnd)
            )
            if (isRemovable) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color = colors.lightGray)
                        .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.width(10.dp).height(2.dp)
                            .background(color = colors.darkGray, shape = RoundedCornerShape(50))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedAppNoTextPreview() {
    val dummyBitmap = createBitmap(64, 64).apply {
        eraseColor(0xFFF1F3F5.toInt())
    }
    val dummyImageBitmap = dummyBitmap.asImageBitmap()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SelectedAppNoText(
            appIcon = dummyImageBitmap,
            isRemovable = true,
        ) {}
        SelectedAppNoText(
            appIcon = dummyImageBitmap,
            isRemovable = false,
        ) {}
    }


}