package com.example.kuitandroidapiexample.ui.register.uistate

import com.example.kuitandroidapiexample.ui.model.AnimalType

data class RegisterUiState(
    val id: Int = 0,
    val url: String = "",
    val name: String = "",
    val reporterName: String = "",
    val state: AnimalType = AnimalType.PROTECT,
    val breed: String="",
    val address: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)