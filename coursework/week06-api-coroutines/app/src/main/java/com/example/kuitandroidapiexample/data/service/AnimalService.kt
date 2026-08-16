package com.example.kuitandroidapiexample.data.service

import com.example.kuitandroidapiexample.data.dto.request.RequestAddAnimalDto
import com.example.kuitandroidapiexample.data.dto.response.BaseResponse
import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDetailDto
import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AnimalService {
    @GET("animals")
    suspend fun getTotalAnimalList() : BaseResponse<List<ResponseAnimalDto>>

    @GET("animals/{id}")
    suspend fun getAnimalDetail(
        @Path("id") id: Int
    ) : BaseResponse<ResponseAnimalDetailDto>

    @POST("animals")
    suspend fun postAddAnimal(
        @Body request: RequestAddAnimalDto
    ) : BaseResponse<Unit>

    @DELETE("animals/{id}")
    suspend fun deleteAnimal(
        @Path("id") id: Int
    ): BaseResponse<Unit>
}