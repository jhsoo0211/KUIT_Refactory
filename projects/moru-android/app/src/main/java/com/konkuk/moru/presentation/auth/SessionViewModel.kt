package com.konkuk.moru.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.data.token.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Exposes the token-backed session state without maintaining a second login flag. */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {
    val isSignedIn: StateFlow<Boolean?> = tokenManager.isSignedIn
        .map<Boolean, Boolean?> { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    suspend fun hasActiveSession(): Boolean = !tokenManager.accessToken().isNullOrBlank()
}
