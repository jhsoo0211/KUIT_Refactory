package com.konkuk.moru.data.token

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
    }

    // ===== [추가] 레거시 저장소(SharedPreferences) 후보들 =====
    private val legacyPrefs by lazy { context.getSharedPreferences("auth", Context.MODE_PRIVATE) }             // [추가]
    private val legacyPrefsAlt by lazy { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }    // [추가]

    // [추가] 레거시에서 access token을 여러 후보 키로 탐색
    private fun findLegacyAccessToken(): String? { // [추가]
        val cands = listOf("access_token", "accessToken", "ACCESS_TOKEN", "Authorization")
        return (cands.firstNotNullOfOrNull { legacyPrefs.getString(it, null) }
            ?: cands.firstNotNullOfOrNull { legacyPrefsAlt.getString(it, null) })
    }

    // [추가] 레거시에서 refresh token을 여러 후보 키로 탐색
    private fun findLegacyRefreshToken(): String? { // [추가]
        val cands = listOf("refresh_token", "refreshToken", "REFRESH_TOKEN")
        return (cands.firstNotNullOfOrNull { legacyPrefs.getString(it, null) }
            ?: cands.firstNotNullOfOrNull { legacyPrefsAlt.getString(it, null) })
    }

    // [추가] DataStore 비어있을 때 1회성 이관 (예외 안전)
    private fun migrateLegacyIfNeeded() { // [추가]
        runBlocking {
            val current = accessToken()
            if (current.isNullOrBlank()) {
                val legacyA = findLegacyAccessToken()
                val legacyR = findLegacyRefreshToken()
                if (!legacyA.isNullOrBlank()) {
                    saveTokens(legacyA, legacyR)
                }
            }
        }
    }

    /** -------- 읽기 (suspend) -------- */
    suspend fun accessToken(): String? =
        context.dataStore.data.map { it[Keys.ACCESS] }.first()

    suspend fun refreshToken(): String? =
        context.dataStore.data.map { it[Keys.REFRESH] }.first()

    /** -------- 읽기 (blocking) : Interceptor/Authenticator 용 -------- */
    fun accessTokenBlocking(): String? { // [변경]
        try { migrateLegacyIfNeeded() } catch (e: Exception) { // [추가]
            android.util.Log.d(
                "createroutine",
                "[authx] migrate failed: exception=${e::class.java.simpleName}"
            )
        }
        return runBlocking { accessToken() }
    }

    fun refreshTokenBlocking(): String? = runBlocking { refreshToken() }

    // [추가] 로그인 여부 간편 체크
    fun isSignedInBlocking(): Boolean = !accessTokenBlocking().isNullOrBlank()

    /** -------- 저장 -------- */
    suspend fun saveTokens(access: String, refresh: String?) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ACCESS] = access
            if (refresh != null) prefs[Keys.REFRESH] = refresh
        }
    }

    suspend fun updateAccess(access: String) {
        context.dataStore.edit { prefs -> prefs[Keys.ACCESS] = access }
    }

    /** -------- 삭제(로그아웃) -------- */
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.ACCESS)
            prefs.remove(Keys.REFRESH)
        }
    }
}
