package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.request.LoginRequestDto
import com.konkuk.moru.data.dto.request.RefreshRequestDto
import com.konkuk.moru.data.dto.request.SignUpRequest
import com.konkuk.moru.data.dto.response.SignUpResponse
import com.konkuk.moru.data.dto.response.login.LoginResponseDto
import com.konkuk.moru.data.dto.response.login.TokenDto
import retrofit2.Response
import com.konkuk.moru.data.dto.response.UserProfile.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/login")
    suspend fun login(@Body body: LoginRequestDto): Response<LoginResponseDto>

    @Headers("Content-Type: application/json")
    @POST("/api/auth/signup")
    suspend fun signUp(@Body body: SignUpRequest): SignUpResponse

    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body body: RefreshRequestDto): Response<TokenDto>

    @GET("/api/users/me") // 서버 스펙 유지
    suspend fun getUserProfile(): UserProfileResponse
}