package com.konkuk.moru.viewmodel.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.data.dto.response.UserProfile.UserProfileResponse
import com.konkuk.moru.data.repositoryimpl.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserProfileResponse?>(null)
    val userInfo: StateFlow<UserProfileResponse?> = _userInfo

    init {
        fetchUserInfo()
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                val profile = authRepo.getUserProfile()
                _userInfo.value = profile
            } catch (e: Exception) {
                // Keep the last known profile when refreshing fails.
            }
        }
    }
}
