package com.example.kuitandroidapiexample

import com.example.kuitandroidapiexample.data.ServicePool.animalService
import com.example.kuitandroidapiexample.data.repository.AnimalRepository
import com.example.kuitandroidapiexample.ui.register.viewmodel.RegisterViewModelFactory

class AppContainer {
    private fun provideApiService() = animalService

    fun provideRepository() = AnimalRepository(provideApiService())
    val registerViewModelFactory = RegisterViewModelFactory(provideRepository())
}