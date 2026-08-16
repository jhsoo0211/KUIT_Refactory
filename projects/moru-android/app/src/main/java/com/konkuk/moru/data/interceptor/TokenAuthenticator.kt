package com.konkuk.moru.data.interceptor

import com.konkuk.moru.data.dto.request.RefreshRequestDto
import com.konkuk.moru.data.service.AuthService
import com.konkuk.moru.data.token.TokenManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Refreshes an expired access token without recursively authenticating the refresh request.
 *
 * The authless Retrofit instance still uses the platform TLS trust store; "authless" means it
 * omits MORU bearer-token interceptors only. A mutex collapses concurrent 401 responses into one
 * refresh, while the request's session generation and a transactional token comparison prevent a
 * stale network response from reviving logout or overwriting a newer login.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("authlessRetrofit") private val authlessRetrofit: Retrofit,
    baseUrl: String
) : Authenticator {

    private val mutex = Mutex()
    private val apiBaseUrl = baseUrl.toHttpUrl()

    override fun authenticate(route: Route?, response: Response): Request? {
        // Recheck after redirects: local request tags can survive even when OkHttp strips a bearer
        // header for a different origin, and must never authorize that redirected destination.
        if (!AuthHeaderPolicy.isSameOrigin(response.request.url, apiBaseUrl)) return null
        // A rejected login, signup, or refresh is an authentication result, not a signal to
        // refresh an unrelated stored session and replay the public request with credentials.
        if (AuthHeaderPolicy.isPublicAuthEndpoint(response.request.url.encodedPath)) return null
        if (responseCount(response) >= 2) return null

        return runBlocking {
            mutex.withLock {
                val requestGeneration = response.request
                    .tag(AuthSessionRequestTag::class.java)
                    ?.generation
                    ?: return@withLock null
                val session = tokenManager.sessionSnapshot()
                if (session.generation != requestGeneration) return@withLock null

                val failed = AuthHeaderPolicy.bearerToken(
                    response.request.header(AuthHeaderPolicy.AUTHORIZATION_HEADER)
                )
                if (!session.accessToken.isNullOrEmpty() && session.accessToken != failed) {
                    // Another 401 already refreshed this same generation while we waited.
                    return@withLock newRequestWithAccess(response.request, session.accessToken)
                }

                val refresh = session.refreshToken ?: return@withLock null
                val authApi = authlessRetrofit.create(AuthService::class.java)

                // Transport failures are retryable and must not destroy an otherwise valid session.
                val res = runCatching {
                    authApi.refreshToken(RefreshRequestDto(refresh))
                }.getOrNull() ?: return@withLock null

                if (res.isSuccessful) {
                    val dto = res.body()
                    if (dto == null || dto.accessToken.isBlank() || dto.refreshToken.isBlank()) {
                        tokenManager.clearIfCurrent(session)
                        return@withLock null
                    }
                    if (!tokenManager.replaceTokensIfCurrent(session, dto.accessToken, dto.refreshToken)) {
                        return@withLock null
                    }
                    return@withLock newRequestWithAccess(response.request, dto.accessToken)
                }

                // Only an explicit credential rejection signs out; 5xx failures keep the session.
                if (res.code() == 401 || res.code() == 403) {
                    tokenManager.clearIfCurrent(session)
                }
                null
            }
        }
    }

    private fun newRequestWithAccess(req: Request, access: String) =
        req.newBuilder()
            .header(
                AuthHeaderPolicy.AUTHORIZATION_HEADER,
                AuthHeaderPolicy.bearerValue(access)
            )
            .build()

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
