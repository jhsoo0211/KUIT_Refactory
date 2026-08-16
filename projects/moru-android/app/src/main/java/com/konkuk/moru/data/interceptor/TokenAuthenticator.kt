package com.konkuk.moru.data.interceptor

import com.konkuk.moru.data.dto.request.RefreshRequestDto
import com.konkuk.moru.data.service.AuthService
import com.konkuk.moru.data.token.TokenManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("authlessRetrofit") private val authlessRetrofit: Retrofit
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        return runBlocking {
            mutex.withLock {
                val latest = tokenManager.accessToken()
                val failed = response.request.header("Authorization")?.removePrefix("Bearer ")
                if (!latest.isNullOrEmpty() && latest != failed) {
                    return@runBlocking newRequestWithAccess(response.request, latest)
                }

                val refresh = tokenManager.refreshToken() ?: return@runBlocking null
                val authApi = authlessRetrofit.create(AuthService::class.java)

                val res = authApi.refreshToken(RefreshRequestDto(refresh))

                if (res.isSuccessful) {
                    val dto = res.body() ?: return@runBlocking null
                    tokenManager.saveTokens(dto.accessToken, dto.refreshToken)
                    return@runBlocking newRequestWithAccess(response.request, dto.accessToken)
                } else {
                    tokenManager.clear()
                    return@runBlocking null
                }
            }
        }
    }

    private fun newRequestWithAccess(req: Request, access: String) =
        req.newBuilder().header("Authorization", "Bearer $access").build()

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) { count++; prior = prior.priorResponse }
        return count
    }
}
