package com.konkuk.moru.data.repositoryimpl

import android.content.Context
import com.konkuk.moru.data.dto.request.LoginRequestDto
import com.konkuk.moru.data.dto.response.login.LoginResponseDto
import com.konkuk.moru.data.dto.response.UserProfile.UserProfileResponse
import com.konkuk.moru.data.service.AuthService
import com.konkuk.moru.data.token.TokenManager
import javax.inject.Inject
import retrofit2.HttpException

class AuthRepository @Inject constructor(
    private val service: AuthService,
    private val tokenManager: TokenManager
) {
    suspend fun getUserProfile(): UserProfileResponse {
        return service.getUserProfile()
    }

    suspend fun loginAndSaveTokens(
        context: Context,
        email: String,
        password: String
    ): LoginResponseDto {
        val response = service.login(LoginRequestDto(email, password))

        if (!response.isSuccessful) {
            throw when (response.code()) {
                400 -> Exception("잘못된 요청입니다. 이메일과 비밀번호가 확인해주세요.")
                401 -> Exception("이메일 또는 비밀번호가 올바르지 않습니다.")
                500 -> Exception("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                else -> Exception("로그인에 실패했습니다. (${response.code()})")
            }
        }

        val loginResponse = response.body() ?: throw Exception("응답 데이터가 없습니다.")

        // Keep login, refresh, request interception, and logout on one token source of truth.
        tokenManager.saveTokens(
            loginResponse.token.accessToken,
            loginResponse.token.refreshToken
        )

        return loginResponse
    }
}
