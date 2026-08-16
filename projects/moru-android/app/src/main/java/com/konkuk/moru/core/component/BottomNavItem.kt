package com.konkuk.moru.core.component

import androidx.annotation.DrawableRes

data class BottomNavItem(
    val title: String, //명칭
    val route: String, //경로
    val iconResId: Int, //선택 시
    val selectedIconResId: Int//미선택 시
)
