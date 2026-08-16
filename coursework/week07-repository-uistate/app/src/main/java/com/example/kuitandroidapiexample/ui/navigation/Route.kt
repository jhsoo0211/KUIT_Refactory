package com.example.kuitandroidapiexample.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Register : Route

    @Serializable
    data class Detail(val index: Int) : Route
}