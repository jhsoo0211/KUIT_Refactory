package com.konkuk.moru.data.interceptor

import okhttp3.HttpUrl

/** Local-only request metadata; it is never serialized into an HTTP header. */
internal class AuthSessionRequestTag(val generation: Long)

/**
 * Defines which requests may carry MORU credentials and how the bearer header is encoded.
 *
 * Only the three known credential-issuing endpoints are public. Requests also have to remain on
 * the configured API origin; this prevents an absolute Retrofit `@Url` from forwarding a bearer
 * token to another host.
 */
internal object AuthHeaderPolicy {
    const val AUTHORIZATION_HEADER = "Authorization"

    private const val BEARER_PREFIX = "Bearer "
    private val publicAuthEndpoints = setOf(
        "/api/auth/login",
        "/api/auth/signup",
        "/api/auth/refresh"
    )

    fun isPublicAuthEndpoint(encodedPath: String): Boolean =
        encodedPath.removeSuffix("/") in publicAuthEndpoints

    fun isSameOrigin(requestUrl: HttpUrl, apiBaseUrl: HttpUrl): Boolean =
        requestUrl.scheme == apiBaseUrl.scheme &&
            requestUrl.host == apiBaseUrl.host &&
            requestUrl.port == apiBaseUrl.port

    fun bearerValue(accessToken: String): String = "$BEARER_PREFIX$accessToken"

    fun bearerToken(headerValue: String?): String? =
        headerValue
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.removePrefix(BEARER_PREFIX)
}
