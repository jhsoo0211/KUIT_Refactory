package com.konkuk.kuitproject1.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.konkuk.kuitproject1.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository

) : ViewModel() {

    fun getHomeData(
        request: Int
    ) {
        viewModelScope.launch {
            homeRepository.getHomeData(request)
                .onSuccess {

                }
                .onFailure {

                }
        }
    }
}
