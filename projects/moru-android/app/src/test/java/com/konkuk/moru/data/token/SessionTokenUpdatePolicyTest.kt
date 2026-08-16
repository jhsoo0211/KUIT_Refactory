package com.konkuk.moru.data.token

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionTokenUpdatePolicyTest {
    @Test
    fun `accepts a refresh result for the unchanged session`() {
        assertTrue(
            SessionTokenUpdatePolicy.matches(
                currentGeneration = 7,
                currentAccessToken = "old-access",
                currentRefreshToken = "old-refresh",
                expectedGeneration = 7,
                expectedAccessToken = "old-access",
                expectedRefreshToken = "old-refresh"
            )
        )
    }

    @Test
    fun `rejects a result after logout or another login`() {
        assertFalse(
            SessionTokenUpdatePolicy.matches(
                currentGeneration = 8,
                currentAccessToken = null,
                currentRefreshToken = null,
                expectedGeneration = 7,
                expectedAccessToken = "old-access",
                expectedRefreshToken = "old-refresh"
            )
        )
        assertFalse(
            SessionTokenUpdatePolicy.matches(
                currentGeneration = 7,
                currentAccessToken = "new-access",
                currentRefreshToken = "new-refresh",
                expectedGeneration = 7,
                expectedAccessToken = "old-access",
                expectedRefreshToken = "old-refresh"
            )
        )
    }
}
