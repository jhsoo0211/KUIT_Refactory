package com.konkuk.moru.data.token

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LegacyTokenMigrationPolicyTest {
    @Test
    fun resolvesTokensWithDeterministicStoreAndKeyPrecedence() {
        val values = mapOf(
            ("auth" to "accessToken") to "primary-access",
            ("auth" to "refresh_token") to "primary-refresh",
            ("user_prefs" to "access_token") to "secondary-access",
            ("user_prefs" to "refreshToken") to "secondary-refresh"
        )

        val result = LegacyTokenMigrationPolicy.resolve { preferenceName, key ->
            values[preferenceName to key]
        }

        assertEquals("primary-access", result?.accessToken)
        assertEquals("primary-refresh", result?.refreshToken)
    }

    @Test
    fun `never combines tokens from different preference stores`() {
        val values = mapOf(
            ("auth" to "access_token") to "primary-access",
            ("user_prefs" to "refresh_token") to "unrelated-refresh"
        )

        val result = LegacyTokenMigrationPolicy.resolve { preferenceName, key ->
            values[preferenceName to key]
        }

        assertEquals("primary-access", result?.accessToken)
        assertNull(result?.refreshToken)
    }

    @Test
    fun ignoresBlankLegacyValues() {
        val values = mapOf(
            ("auth" to "access_token") to "   ",
            ("user_prefs" to "ACCESS_TOKEN") to "fallback-access"
        )

        val result = LegacyTokenMigrationPolicy.resolve { preferenceName, key ->
            values[preferenceName to key]
        }

        assertEquals("fallback-access", result?.accessToken)
        assertNull(result?.refreshToken)
    }

    @Test
    fun refusesRefreshTokenWithoutAnAccessToken() {
        val values = mapOf(("auth" to "refresh_token") to "orphan-refresh")

        assertNull(
            LegacyTokenMigrationPolicy.resolve { preferenceName, key ->
                values[preferenceName to key]
            }
        )
    }
}
