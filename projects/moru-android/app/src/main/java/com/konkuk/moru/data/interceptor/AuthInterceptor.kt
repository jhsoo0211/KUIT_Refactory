package com.konkuk.moru.data.interceptor

import com.konkuk.moru.data.token.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val path = original.url.encodedPath

        val token: String? = tokenManager.accessTokenBlocking() // [유지]

        // 로그인/토큰 발급 계열만 제외
        val isAuthExcluded = path.startsWith("/api/auth") || path.startsWith("/auth")
        val shouldAttach = !isAuthExcluded && !token.isNullOrBlank()

        val req = if (shouldAttach) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else original

        return chain.proceed(req)
    }
}
