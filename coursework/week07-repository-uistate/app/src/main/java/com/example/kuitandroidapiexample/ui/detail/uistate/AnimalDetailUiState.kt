package com.example.kuitandroidapiexample.ui.detail.uistate

import com.example.kuitandroidapiexample.ui.model.AnimalType

data class AnimalDetailUiState(
    val url: String = "",
    val animalName: String = "",
    val animalType: AnimalType = AnimalType.PROTECT,
    val address: String = "",
    val reporterName: String = "",
    var isDelete : Boolean = false
)
