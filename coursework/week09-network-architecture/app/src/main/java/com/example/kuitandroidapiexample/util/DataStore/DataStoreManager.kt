package com.example.kuitandroidapiexample.util

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.kuitandroidapiexample.util.DataStore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreManager {
    // 동적 키 생성
    private fun keyOf(name: String) = stringPreferencesKey(name)

    // 저장 (suspend)
    suspend fun saveValue(context: Context, key: String, value: String) {
        context.dataStore.edit { prefs ->
            prefs[keyOf(key)] = value
        }
    }

    // 읽기 (Flow)
    fun getValue(context: Context, key: String): Flow<String?> =
        context.dataStore.data
            .map { prefs -> prefs[keyOf(key)] }

    // 삭제 (suspend)
    suspend fun deleteValue(context: Context, key: String) {
        context.dataStore.edit { prefs ->
            prefs.remove(keyOf(key))
        }
    }
}