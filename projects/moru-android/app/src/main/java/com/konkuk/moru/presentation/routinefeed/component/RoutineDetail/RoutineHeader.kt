package com.konkuk.moru.presentation.routinefeed.component.RoutineDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.konkuk.moru.R
import com.konkuk.moru.data.model.Routine

@Composable
fun RoutineHeader(
    routine: Routine,
    onProfileClick: (authorId: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = routine.imageUrl,
            contentDescription = "루틴 대표 이미지",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_profile_with_background),
            error = painterResource(id = R.drawable.ic_launcher_background)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.3f to Color.Transparent,
                            1f to Color.White.copy(alpha = 0.7f) // alpha 값 조정
                        )
                    )
                )
        )


        RoutineInfoOverlay(
            modifier = Modifier.padding(16.dp),
            routine = routine,
            onProfileClick = onProfileClick
        )
    }
}