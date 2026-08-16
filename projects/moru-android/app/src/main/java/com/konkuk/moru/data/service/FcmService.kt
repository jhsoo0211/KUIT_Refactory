package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.request.FcmTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmService {
    @POST("api/user/fcm-token")
    suspend fun registerFcmToken(@Body fcmToken: FcmTokenRequest): Response<Unit>
}