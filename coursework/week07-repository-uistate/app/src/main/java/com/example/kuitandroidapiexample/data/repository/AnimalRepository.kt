package com.example.kuitandroidapiexample.data.repository

import com.example.kuitandroidapiexample.data.dto.request.RequestAddAnimalDto
import com.example.kuitandroidapiexample.data.dto.response.BaseResponse
import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDto
import com.example.kuitandroidapiexample.data.service.AnimalService
import com.example.kuitandroidapiexample.ui.model.AnimalType

class AnimalRepository(
    private val animalService: AnimalService
) {
    suspend fun getAnimal(id: Int) = runCatching { animalService.getAnimalDetail(id) }

    suspend fun addAnimal(
        id: Int,
        url: String,
        name: String,
        state: AnimalType,
        breed: String,
        address: String
    ): Result<Unit> {
        return runCatching {
            animalService.postAddAnimal(
                RequestAddAnimalDto(
                    id = id,
                    url = url,
                    name = name,
                    state = state,
                    breed = breed,
                    address = address
                )
            )
        }
    }

    suspend fun getTotalAnimalList(): Result<BaseResponse<List<ResponseAnimalDto>>> {
        return runCatching {
            animalService.getTotalAnimalList()
        }
    }

    suspend fun deleteAnimal(id: Int) = runCatching { animalService.deleteAnimal(id) }
}