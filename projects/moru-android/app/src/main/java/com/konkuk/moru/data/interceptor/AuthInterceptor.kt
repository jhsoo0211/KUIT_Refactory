package com.konkuk.moru.data.interceptor

import com.konkuk.moru.data.token.TokenManager
import okhttp3.Interceptor
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    baseUrl: String
) : Interceptor {
    private val apiBaseUrl = baseUrl.toHttpUrl()

    /**
     * Adds the current access token only to protected API calls.
     *
     * OkHttp interceptors are synchronous, so the DataStore bridge is blocking by design and
     * must run on OkHttp's worker thread. Public login, signup, and refresh calls bypass token
     * access entirely. Any caller-provided Authorization value is removed so TokenManager remains
     * the only credential authority and stale headers cannot leak into authentication requests.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val path = original.url.encodedPath

        if (
            AuthHeaderPolicy.isPublicAuthEndpoint(path) ||
            !AuthHeaderPolicy.isSameOrigin(original.url, apiBaseUrl)
        ) {
            return chain.proceed(
                original.newBuilder()
                    .removeHeader(AuthHeaderPolicy.AUTHORIZATION_HEADER)
                    .build()
            )
        }

        val session = tokenManager.sessionSnapshotBlocking()
        val token = session.accessToken
        val request = original.newBuilder()
            .removeHeader(AuthHeaderPolicy.AUTHORIZATION_HEADER)
            .tag(AuthSessionRequestTag::class.java, AuthSessionRequestTag(session.generation))
            .apply {
                if (!token.isNullOrBlank()) {
                    header(
                        AuthHeaderPolicy.AUTHORIZATION_HEADER,
                        AuthHeaderPolicy.bearerValue(token)
                    )
                }
            }
            .build()

        return chain.proceed(request)
    }
}
