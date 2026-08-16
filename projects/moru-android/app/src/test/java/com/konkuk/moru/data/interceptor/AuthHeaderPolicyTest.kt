package com.konkuk.moru.data.interceptor

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import okhttp3.HttpUrl.Companion.toHttpUrl

class AuthHeaderPolicyTest {
    @Test
    fun publicAuthPathsUseAnExactAllowList() {
        assertTrue(AuthHeaderPolicy.isPublicAuthEndpoint("/api/auth/login"))
        assertTrue(AuthHeaderPolicy.isPublicAuthEndpoint("/api/auth/signup/"))
        assertTrue(AuthHeaderPolicy.isPublicAuthEndpoint("/api/auth/refresh"))

        assertFalse(AuthHeaderPolicy.isPublicAuthEndpoint("/api/auth"))
        assertFalse(AuthHeaderPolicy.isPublicAuthEndpoint("/api/author"))
        assertFalse(AuthHeaderPolicy.isPublicAuthEndpoint("/api/auth/logout"))
        assertFalse(AuthHeaderPolicy.isPublicAuthEndpoint("/api/routines"))
    }

    @Test
    fun bearerCredentialsStayOnTheConfiguredOrigin() {
        val apiBaseUrl = "https://api.example.com/".toHttpUrl()

        assertTrue(
            AuthHeaderPolicy.isSameOrigin(
                "https://api.example.com/routines".toHttpUrl(),
                apiBaseUrl
            )
        )
        assertFalse(
            AuthHeaderPolicy.isSameOrigin(
                "https://uploads.example.com/image".toHttpUrl(),
                apiBaseUrl
            )
        )
        assertFalse(
            AuthHeaderPolicy.isSameOrigin(
                "https://api.example.com:8443/routines".toHttpUrl(),
                apiBaseUrl
            )
        )
    }

    @Test
    fun bearerHeaderRoundTripsOnlyForBearerScheme() {
        val header = AuthHeaderPolicy.bearerValue("access-token")

        assertEquals("Bearer access-token", header)
        assertEquals("access-token", AuthHeaderPolicy.bearerToken(header))
        assertNull(AuthHeaderPolicy.bearerToken("Basic credentials"))
        assertNull(AuthHeaderPolicy.bearerToken(null))
    }
}
