package com.konkuk.moru.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.core.datastore.LoginPreference
import com.konkuk.moru.core.datastore.OnboardingPreference
import com.konkuk.moru.data.repositoryimpl.AuthRepository as ImplAuthRepository
import com.konkuk.moru.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: ImplAuthRepository,
    private val userRepository: UserRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun login(
        email: String,
        password: String,
        context: Context,
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                val resp = authRepository.loginAndSaveTokens(context, email, password)

                if (resp.isOnboarding) {
                    OnboardingPreference.setOnboardingComplete(context) // [유지]
                }

                LoginPreference.setLoggedIn(context, true)             // [유지]
                // LoginPreference.saveAccessToken(...), saveRefreshToken(...)  // [삭제] 토큰은 TokenManager가 담당

                userSessionManager.setLoggedIn(true)
                _isLoggedIn.value = true

                runCatching { userRepository.getUserProfile() }
                    .onSuccess { profile -> userSessionManager.setUserProfile(profile) }

                onSuccess(resp.isOnboarding)
            }.onFailure { e ->
                onFailure(e.message ?: "로그인 실패")
            }
        }
    }
}