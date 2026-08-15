package com.konkuk.moru.presentation.home.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HomeTutorialOverlayView(
    modifier: Modifier = Modifier,
    holes: List<TutorialOverlayView.HolePx>
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            TutorialOverlayView(context).apply {
                this.holes = holes
            }
        },
        update = {
            it.holes = holes
        }
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun HomeTutorialOverlayViewPreview() {
    val sampleHoles = listOf(
        TutorialOverlayView.HolePx(
            left = 50f,
            top = 250f,
            right = 310f,
            bottom = 290f,
            cornerRadius = 16f,
            isCircle = false
        ),
        TutorialOverlayView.HolePx(
            left = 281f,
            top = 641f,
            right = 344f,
            bottom = 704f,
            isCircle = true
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        HomeTutorialOverlayView(
            holes = sampleHoles,
        )
    }
}