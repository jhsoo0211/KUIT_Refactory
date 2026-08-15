package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.request.MyActUpdateMeRequest
import com.konkuk.moru.data.dto.response.MyActNicknameAvailabilityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MyActUserService {
    @PATCH("/api/user/me")
    suspend fun patchMe(@Body body: MyActUpdateMeRequest): Response<Unit> // 200/204 대응

    @GET("/api/user/nickname/{nickname}")
    suspend fun checkNickname(
        @Path("nickname") nickname: String // 한글 포함 시 Retrofit이 URL-encode 처리
    ): Response<MyActNicknameAvailabilityResponse>
}
