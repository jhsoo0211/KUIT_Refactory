package com.konkuk.kuitproject1.presentation.navigation

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.konkuk.kuitproject1.presentation.diary.DiaryScreen
import com.konkuk.kuitproject1.presentation.home.HomeScreen
import com.konkuk.kuitproject1.presentation.my.MyScreen

@Composable
fun KuitNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route
    ) {
        composable(route = Route.Home.route) {
            HomeScreen(modifier = modifier)
        }

        composable(route = Route.Diary.route) {
            DiaryScreen(modifier = modifier)
        }

        composable(route = Route.My.route) {
            MyScreen(modifier = modifier)
        }
    }
}