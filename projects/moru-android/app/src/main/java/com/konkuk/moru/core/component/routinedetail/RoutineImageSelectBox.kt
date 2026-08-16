package com.konkuk.moru.core.component.routinedetail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors

@Composable
fun RoutineImageSelectBox(
    selectedImageUri: Uri? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(105.dp)
            .fillMaxHeight()
            .background(colors.veryLightGray, RoundedCornerShape(4.dp))
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedImageUri),
                contentDescription = "선택된 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_routine_image_default),
                contentDescription = "기본 이미지",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun RoutineImageSelectBoxPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        verticalAlignment = Alignment.Top
    ) {
        RoutineImageSelectBox() {}
    }

}
