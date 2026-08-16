package com.konkuk.moru.presentation.login

import com.konkuk.moru.data.dto.response.UserProfile.UserProfileResponse
import com.konkuk.moru.data.token.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor(
    private val tokenManager: TokenManager
) {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> = _userProfile

    init {
        _isLoggedIn.value = !tokenManager.accessTokenBlocking().isNullOrEmpty()
    }

    fun setLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }

    fun setUserProfile(profile: UserProfileResponse) {
        _userProfile.value = profile
    }

    fun getUserProfile(): UserProfileResponse? {
        return _userProfile.value
    }

    fun clearUserProfile() {
        _userProfile.value = null
    }
}
