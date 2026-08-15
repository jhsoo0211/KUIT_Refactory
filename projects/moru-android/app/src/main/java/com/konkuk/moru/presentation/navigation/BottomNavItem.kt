package com.konkuk.moru.presentation.navigation

// BottomNavItem.kt (또는 AppNavGraph.kt 파일 내부에 정의)
data class BottomNavItem(
    val title: String,
    val route: String,
    val iconResId: Int, // 일반 상태 아이콘 리소스 ID
    val selectedIconResId: Int // 선택된 상태 아이콘 리소스 ID
)