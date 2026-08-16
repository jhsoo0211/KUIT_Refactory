// ui/register/viewmodel/RegisterViewModel.kt
package com.example.kuitandroidapiexample.ui.register.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kuitandroidapiexample.data.dto.request.RequestAddAnimalDto
import com.example.kuitandroidapiexample.data.repository.AnimalRepository
import com.example.kuitandroidapiexample.ui.model.AnimalType
import com.example.kuitandroidapiexample.ui.register.uistate.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val repository: AnimalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()


    fun updateUrl(url: String) {
        _uiState.update { it.copy(url = url) }
    }

    fun updateAnimalName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateAddress(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    fun updateReporterName(name: String) {
        _uiState.update { it.copy(reporterName = name) }
    }

    fun updateAnimalType(type: AnimalType) {
        _uiState.update { it.copy(state = type) }
    }

    fun updatebreed(type: String) {
        _uiState.update { it.copy(breed = type) }
    }


    fun postAnimal() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.addAnimal(
                id = state.id,
                url = state.url,
                name = state.reporterName,
                state = state.state,
                breed = state.breed,
                address = state.address
            ).fold(
                onSuccess = {
                    _uiState.update { it.copy(isSuccess = true) }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(error = throwable.message ?: "등록 실패")
                    }
                }
            )
        }
    }


    fun registerAnimal() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.addAnimal(
                id = uiState.value.id,
                url = uiState.value.url,
                name = uiState.value.name,
                state = uiState.value.state,
                breed = uiState.value.breed,
                address = uiState.value.address

            )
                .onSuccess { _: Any? ->
                    _uiState.update { it.copy(isSuccess = true, isLoading = false) }
                }
                .onFailure { ex: Throwable ->
                    Log.e("RegisterViewModel", ex.message ?: "Unknown error")
                    _uiState.update { it.copy(error = ex.message, isLoading = false) }
                }
        }

    }
}


class RegisterViewModelFactory(
    private val repository: AnimalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(repository) as T
    }
}