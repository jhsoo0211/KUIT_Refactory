package com.example.kuitandroidapiexample.ui.detail.uistate

import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDetailDto

fun ResponseAnimalDetailDto.toUiState() = AnimalDetailUiState(
    url = this.url,
    animalName = this.breed,
    animalType = this.state,
    address = this.address,
    reporterName = this.name
)