package com.konkuk.moru.presentation.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.konkuk.moru.R

@Composable
fun Avatar(url: String?, modifier: Modifier = Modifier) {
    if (url.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.ic_avatar),
            contentDescription = "avatar",
            modifier = modifier.size(36.dp).clip(CircleShape)
        )
        return
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .listener(
                onError = { _, res ->
                    Log.e(
                        "Avatar",
                        "Load failed: exception=${res.throwable?.javaClass?.simpleName ?: "Unknown"}"
                    )
                }
            )
            .build(),
        contentDescription = "avatar",
        placeholder = painterResource(R.drawable.ic_avatar),
        error = painterResource(R.drawable.ic_avatar),
        contentScale = ContentScale.Crop,
        modifier = modifier.size(36.dp).clip(CircleShape)
    )
}
