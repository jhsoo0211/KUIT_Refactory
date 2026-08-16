package com.konkuk.moru.data.repositoryimpl

import android.util.Log
import com.konkuk.moru.data.dto.request.FcmTokenRequest
import com.konkuk.moru.data.service.FcmService
import com.konkuk.moru.domain.repository.FcmRepository
import com.konkuk.moru.data.token.TokenManager        // [추가]
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val fcmService: FcmService,
    private val tokenManager: TokenManager            // [추가]
) : FcmRepository {
    override suspend fun registerFcmToken(token: String): Result<Unit> {
        return try {
            if (!tokenManager.isSignedInBlocking()) { // [추가] 인증 가드
                Log.d("createroutine", "[fcm] skip send (no token)")
                return Result.failure(IllegalStateException("Not signed in"))
            }
            val response = fcmService.registerFcmToken(FcmTokenRequest(token))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Throwable("FCM token registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}