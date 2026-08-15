package com.konkuk.moru.core.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors

@Composable
fun ProfileSettingCard(
    imageUri: Uri? = null,
    placeholderRes: Int = R.drawable.ic_basic_profile,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Profile Image",
            placeholder = painterResource(id = placeholderRes),
            error = painterResource(id = placeholderRes),
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .clip(CircleShape) // 원형 잘라내기
                .background(color = colors.lightGray, shape = CircleShape),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.ic_profile_edit),
            contentDescription = "Edit Profile",
            modifier = Modifier
                .padding(1.dp)
                .width(20.dp)
                .height(20.dp)
                .align(Alignment.TopEnd) // 원 밖으로 나가도 잘림 없음
        )
    }
}

@Preview
@Composable
private fun ProfileSettingCardPreview() {
    ProfileSettingCard { }
}
