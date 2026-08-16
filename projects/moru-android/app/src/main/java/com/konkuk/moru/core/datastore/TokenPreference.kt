package com.konkuk.moru.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TOKEN_PREFS = "token_prefs"
private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_PREFS)

object TokenPreference {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    fun getAccessToken(context: Context): Flow<String?> {
        return context.tokenDataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }

    fun getRefreshToken(context: Context): Flow<String?> {
        return context.tokenDataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }
    }

    suspend fun setTokens(context: Context, accessToken: String, refreshToken: String) {
        context.tokenDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun clearTokens(context: Context) {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }
}
