package com.example.kuitandroidapiexample.util

import android.content.Context
import android.system.Os.remove
import androidx.compose.runtime.key
import androidx.core.content.edit
import coil3.memory.MemoryCache

object SharedPreferenceManager {
    fun saveValue(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        prefs.edit {
            putString(key, value)
        }
    }

    fun getValue(context: Context, key: String): String? {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }


    fun deleteValue(context: Context, key: String) {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        prefs.edit{ remove(key) }
    }
}