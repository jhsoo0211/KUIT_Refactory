package com.konkuk.moru.presentation.auth

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konkuk.moru.core.datastore.OnboardingPreference
import com.konkuk.moru.presentation.navigation.Route
import kotlinx.coroutines.flow.first

@Composable
fun AuthCheckScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        try {
            val isSignedIn = sessionViewModel.hasActiveSession()
            val isOnboarded = OnboardingPreference
                .isOnboardingComplete(navController.context)
                .first()

            val targetRoute = when {
                !isSignedIn -> Route.Login.route
                !isOnboarded -> Route.Onboarding.route
                else -> Route.Main.route
            }

            Log.d(
                "AuthCheckScreen",
                "Session resolved: signedIn=$isSignedIn, onboarded=$isOnboarded"
            )
            navController.navigate(targetRoute) {
                popUpTo(0) { inclusive = true }
            }
        } catch (e: Exception) {
            Log.e(
                "AuthCheckScreen",
                "Session resolution failed: exception=${e.javaClass.simpleName}"
            )
            navController.navigate(Route.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
