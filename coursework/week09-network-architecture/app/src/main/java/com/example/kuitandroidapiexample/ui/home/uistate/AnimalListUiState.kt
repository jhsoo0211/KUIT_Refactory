package com.example.kuitandroidapiexample.ui.home.uistate

import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDto
import com.example.kuitandroidapiexample.data.dto.response.BaseResponse


data class AnimalListUiState(
    val isLoading: Boolean = false,
    val data: BaseResponse<List<ResponseAnimalDto>>? = null,
    val error: String? = null
)