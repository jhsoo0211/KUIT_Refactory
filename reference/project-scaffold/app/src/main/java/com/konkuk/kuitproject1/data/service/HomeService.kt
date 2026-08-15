package com.konkuk.kuitproject1.data.service

import com.konkuk.kuitproject1.data.dto.respose.BaseRespose
import com.konkuk.kuitproject1.data.dto.respose.ResposeHomeDto
import retrofit2.http.Body
import retrofit2.http.GET

interface HomeService{
    @GET("home")
    suspend fun getHomeData(
        @Body request:Int // homeId 올수 있음
    ):BaseRespose<ResposeHomeDto>
}