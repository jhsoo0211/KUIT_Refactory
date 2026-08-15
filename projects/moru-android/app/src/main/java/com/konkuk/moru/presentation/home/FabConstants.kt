package com.konkuk.moru.presentation.home

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object FabConstants {
    val FabPaddingBottom: Dp = 16.dp
    val BottomBarHeight: Dp = 80.dp

    val FabTotalBottomPadding: Dp
        get() = FabPaddingBottom + BottomBarHeight
}