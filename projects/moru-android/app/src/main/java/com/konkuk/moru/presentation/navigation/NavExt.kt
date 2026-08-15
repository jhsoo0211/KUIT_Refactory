package com.konkuk.moru.presentation.navigation

import androidx.navigation.NavController

//스택이 있으면 뒤로 가고 없다면 바로 홈으로
fun NavController.navigateUpOrHome() {
    val popped = popBackStack()
    if (!popped) {
        navigate(Route.Home.route) {
            popUpTo(graph.startDestinationId) { inclusive = false }
            launchSingleTop = true
            restoreState = true
        }
    }
}