package com.konkuk.moru.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val LOGIN_PREFS = "login_prefs"
private val Context.loginDataStore by preferencesDataStore(name = LOGIN_PREFS)

object LoginPreference {
    private val LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    fun isLoggedIn(context: Context): Flow<Boolean> {
        return context.loginDataStore.data.map { prefs ->
            prefs[LOGGED_IN_KEY] ?: false
        }
    }

    suspend fun setLoggedIn(context: Context, value: Boolean) {
        context.loginDataStore.edit { prefs ->
            prefs[LOGGED_IN_KEY] = value
        }
    }

    suspend fun saveAccessToken(context: Context, token: String) {
        context.loginDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun saveRefreshToken(context: Context, token: String) {
        context.loginDataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = token
        }
    }

    fun getAccessToken(context: Context): Flow<String?> {
        return context.loginDataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }

    fun getRefreshToken(context: Context): Flow<String?> {
        return context.loginDataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }
    }

    // 모든 로그인 데이터 삭제 (개발용)
    suspend fun clearAllData(context: Context) {
        context.loginDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}