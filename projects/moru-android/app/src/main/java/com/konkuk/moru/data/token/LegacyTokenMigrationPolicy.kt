package com.konkuk.moru.data.token

/** Token container intentionally has no generated toString() that could expose credentials. */
internal class LegacyTokens(
    val accessToken: String,
    val refreshToken: String?
)

/**
 * Describes the historical preference names and keys supported by the one-time migration.
 * Keeping discovery pure makes precedence and blank-value handling independently testable.
 */
internal object LegacyTokenMigrationPolicy {
    val preferenceNames = listOf("auth", "user_prefs")
    val accessTokenKeys = listOf("access_token", "accessToken", "ACCESS_TOKEN", "Authorization")
    val refreshTokenKeys = listOf("refresh_token", "refreshToken", "REFRESH_TOKEN")
    val allTokenKeys = (accessTokenKeys + refreshTokenKeys).distinct()

    fun resolve(read: (preferenceName: String, key: String) -> String?): LegacyTokens? =
        preferenceNames.firstNotNullOfOrNull { preferenceName ->
            val accessToken = firstNonBlank(accessTokenKeys) { key ->
                read(preferenceName, key)
            } ?: return@firstNotNullOfOrNull null

            // Tokens from different stores may belong to different accounts. Migrate only the
            // refresh token beside the selected access token, even when another store has one.
            val refreshToken = firstNonBlank(refreshTokenKeys) { key ->
                read(preferenceName, key)
            }
            LegacyTokens(accessToken = accessToken, refreshToken = refreshToken)
        }

    private fun firstNonBlank(
        keys: List<String>,
        read: (key: String) -> String?
    ): String? = keys.firstNotNullOfOrNull { key ->
        read(key)?.takeIf(String::isNotBlank)
    }
}
