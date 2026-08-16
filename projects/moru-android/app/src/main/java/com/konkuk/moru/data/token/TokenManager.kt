package com.konkuk.moru.data.token

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")
private val Context.legacyLoginDataStore by preferencesDataStore(name = "login_prefs")
private val Context.legacyTokenDataStore by preferencesDataStore(name = "token_prefs")

/** In-memory view of one persisted session. It deliberately has no token-revealing toString(). */
internal class AuthSessionSnapshot(
    val accessToken: String?,
    val refreshToken: String?,
    val generation: Long
)

/**
 * Single source of truth for MORU session tokens.
 *
 * Preferences DataStore is app-private but is not encrypted storage. This portfolio hardening
 * removes hardcoded and logged credentials, but a production threat model should migrate these
 * values to an Android Keystore-backed solution. Callers must never log values returned here.
 */
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
        val SESSION_GENERATION = longPreferencesKey("session_generation")
        val LEGACY_MIGRATION_COMPLETE = booleanPreferencesKey("legacy_token_migration_complete")
    }

    private val legacyMigrationMutex = Mutex()

    @Volatile
    private var legacyMigrationChecked = false

    /**
     * Imports historical SharedPreferences at most once per installation.
     *
     * The completion marker and optional token copy are committed in one DataStore transaction.
     * Legacy keys are deleted only after that transaction completes, so an interrupted write never
     * destroys the sole remaining credential copy and a later logout cannot resurrect it.
     */
    private suspend fun migrateLegacyIfNeeded() {
        if (legacyMigrationChecked) return

        legacyMigrationMutex.withLock {
            if (legacyMigrationChecked) return

            val storedPreferences = context.dataStore.data.first()
            if (storedPreferences[Keys.LEGACY_MIGRATION_COMPLETE] == true) {
                // Cleanup is idempotent and retries if a prior process ended before apply() flushed.
                clearLegacyTokenStores()
                legacyMigrationChecked = true
                return
            }

            val legacyTokens = if (storedPreferences[Keys.ACCESS].isNullOrBlank()) {
                LegacyTokenMigrationPolicy.resolve(::readLegacyValue)
            } else {
                null
            }

            context.dataStore.edit { currentPreferences ->
                if (currentPreferences[Keys.ACCESS].isNullOrBlank() && legacyTokens != null) {
                    currentPreferences[Keys.ACCESS] = legacyTokens.accessToken
                    if (legacyTokens.refreshToken != null) {
                        currentPreferences[Keys.REFRESH] = legacyTokens.refreshToken
                    } else {
                        currentPreferences.remove(Keys.REFRESH)
                    }
                    currentPreferences[Keys.SESSION_GENERATION] =
                        nextGeneration(currentPreferences[Keys.SESSION_GENERATION])
                }
                currentPreferences[Keys.LEGACY_MIGRATION_COMPLETE] = true
            }

            clearLegacyTokenStores()
            legacyMigrationChecked = true
        }
    }

    val isSignedIn: Flow<Boolean> = flow {
        // Delay the first auth-state emission until one-time migration has settled. Otherwise a
        // cold start could briefly report signed-out and eject a valid migrated session.
        sessionSnapshot()
        emitAll(context.dataStore.data.map { !it[Keys.ACCESS].isNullOrBlank() })
    }
        .distinctUntilChanged()

    internal suspend fun sessionSnapshot(): AuthSessionSnapshot {
        try {
            migrateLegacyIfNeeded()
        } catch (e: Exception) {
            android.util.Log.d(
                "TokenManager",
                "Legacy token migration failed: exception=${e::class.java.simpleName}"
            )
        }

        return context.dataStore.data.map { preferences ->
            AuthSessionSnapshot(
                accessToken = preferences[Keys.ACCESS],
                refreshToken = preferences[Keys.REFRESH],
                generation = preferences[Keys.SESSION_GENERATION] ?: 0L
            )
        }.first()
    }

    suspend fun accessToken(): String? = sessionSnapshot().accessToken

    suspend fun refreshToken(): String? = sessionSnapshot().refreshToken

    /** Blocking bridge for synchronous OkHttp and startup call sites. Never call from UI work. */
    internal fun sessionSnapshotBlocking(): AuthSessionSnapshot = runBlocking { sessionSnapshot() }

    fun accessTokenBlocking(): String? = sessionSnapshotBlocking().accessToken

    fun refreshTokenBlocking(): String? = runBlocking { refreshToken() }

    fun isSignedInBlocking(): Boolean = !accessTokenBlocking().isNullOrBlank()

    /** Replaces the complete token pair; a missing refresh token removes any stale prior value. */
    suspend fun saveTokens(access: String, refresh: String?) {
        require(access.isNotBlank()) { "Access token must not be blank" }
        context.dataStore.edit { prefs ->
            prefs[Keys.ACCESS] = access
            if (refresh != null) {
                prefs[Keys.REFRESH] = refresh
            } else {
                prefs.remove(Keys.REFRESH)
            }
            prefs[Keys.SESSION_GENERATION] = nextGeneration(prefs[Keys.SESSION_GENERATION])
        }
    }

    /**
     * Commits a refresh response only if logout or a different login has not changed the session.
     * DataStore serializes the comparison and replacement in this single edit transaction.
     */
    internal suspend fun replaceTokensIfCurrent(
        expected: AuthSessionSnapshot,
        access: String,
        refresh: String?
    ): Boolean {
        if (access.isBlank() || expected.refreshToken == null) return false

        var replaced = false
        context.dataStore.edit { prefs ->
            if (
                SessionTokenUpdatePolicy.matches(
                    currentGeneration = prefs[Keys.SESSION_GENERATION] ?: 0L,
                    currentAccessToken = prefs[Keys.ACCESS],
                    currentRefreshToken = prefs[Keys.REFRESH],
                    expectedGeneration = expected.generation,
                    expectedAccessToken = expected.accessToken,
                    expectedRefreshToken = expected.refreshToken
                )
            ) {
                prefs[Keys.ACCESS] = access
                if (refresh != null) {
                    prefs[Keys.REFRESH] = refresh
                } else {
                    prefs.remove(Keys.REFRESH)
                }
                replaced = true
            }
        }
        return replaced
    }

    /** Clears an invalid session only when it is still the session that produced the response. */
    internal suspend fun clearIfCurrent(expected: AuthSessionSnapshot): Boolean {
        val expectedRefresh = expected.refreshToken ?: return false
        var cleared = false
        context.dataStore.edit { prefs ->
            if (
                SessionTokenUpdatePolicy.matches(
                    currentGeneration = prefs[Keys.SESSION_GENERATION] ?: 0L,
                    currentAccessToken = prefs[Keys.ACCESS],
                    currentRefreshToken = prefs[Keys.REFRESH],
                    expectedGeneration = expected.generation,
                    expectedAccessToken = expected.accessToken,
                    expectedRefreshToken = expectedRefresh
                )
            ) {
                prefs.remove(Keys.ACCESS)
                prefs.remove(Keys.REFRESH)
                prefs[Keys.SESSION_GENERATION] = nextGeneration(expected.generation)
                prefs[Keys.LEGACY_MIGRATION_COMPLETE] = true
                cleared = true
            }
        }
        if (cleared) clearLegacyTokenStores()
        return cleared
    }

    /**
     * Clears both current and historical stores while holding the migration lock.
     * Marking migration complete prevents a concurrent or later read from undoing logout.
     */
    suspend fun clear() {
        legacyMigrationMutex.withLock {
            context.dataStore.edit { prefs ->
                prefs.remove(Keys.ACCESS)
                prefs.remove(Keys.REFRESH)
                prefs[Keys.SESSION_GENERATION] = nextGeneration(prefs[Keys.SESSION_GENERATION])
                prefs[Keys.LEGACY_MIGRATION_COMPLETE] = true
            }
            clearLegacyTokenStores()
            legacyMigrationChecked = true
        }
    }

    private fun readLegacyValue(preferenceName: String, key: String): String? =
        context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getString(key, null)

    /** Removes historical token copies; they are not combined because ownership is ambiguous. */
    private suspend fun clearLegacyTokenStores() {
        LegacyTokenMigrationPolicy.preferenceNames.forEach { preferenceName ->
            val editor = context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
            LegacyTokenMigrationPolicy.allTokenKeys.forEach(editor::remove)
            editor.apply()
        }
        context.legacyLoginDataStore.edit { it.clear() }
        context.legacyTokenDataStore.edit { it.clear() }
    }

    private fun nextGeneration(current: Long?): Long =
        if (current == Long.MAX_VALUE) 0L else (current ?: 0L) + 1L
}
