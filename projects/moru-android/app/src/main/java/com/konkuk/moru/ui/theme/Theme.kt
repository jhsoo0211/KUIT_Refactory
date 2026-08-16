package com.konkuk.moru.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object MORUTheme {
    val colors: MoruColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMoruColorsProvider.current

    val typography: MoruTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMoruTypographyProvider.current
}

@Composable
fun MORUTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}