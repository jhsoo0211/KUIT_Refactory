package com.example.kuitandroidapiexample.ui.detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kuitandroidapiexample.data.repository.AnimalRepository
import com.example.kuitandroidapiexample.ui.detail.uistate.AnimalDetailUiState
import com.example.kuitandroidapiexample.ui.detail.uistate.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalDetailViewModel(
    private val animalRepository: AnimalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimalDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun getAnimalDetail(id: Int) {
        viewModelScope.launch {
            animalRepository.getAnimal(id).fold(
                onSuccess = { data ->
                    _uiState.value = data.data.toUiState()
                },
                onFailure = { error ->
                    Log.e("okhttpError", error.message.toString())
                }
            )
        }
    }

    fun deleteAnimal(id: Int) {
        viewModelScope.launch {
            animalRepository.deleteAnimal(id).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(isDelete = true)
                    }
                },
                onFailure = { error ->
                    Log.e("okhttpError", error.message.toString())
                }
            )
        }
    }
}

class AnimalDetailViewModelFactory(
    private val animalRepository: AnimalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AnimalDetailViewModel(animalRepository) as T
}