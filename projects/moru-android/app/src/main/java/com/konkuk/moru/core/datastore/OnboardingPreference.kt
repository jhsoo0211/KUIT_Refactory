package com.konkuk.moru.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_prefs"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object OnboardingPreference {
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_complete")

    fun isOnboardingComplete(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[ONBOARDING_KEY] ?: false
        }
    }

    suspend fun setOnboardingComplete(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_KEY] = true
        }
    }
}