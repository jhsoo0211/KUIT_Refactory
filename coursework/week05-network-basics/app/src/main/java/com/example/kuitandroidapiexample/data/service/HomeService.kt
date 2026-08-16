package com.example.kuitandroidapiexample.data.service

import com.example.kuitandroidapiexample.data.dto.ResponseAnimalListDto
import com.example.kuitandroidapiexample.data.dto.request.RequestAnimalDto
import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDetailDto
import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDetailWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeService {
    // 모든 동물 조회
    @GET("animals") //GET DELET 도 가능
    fun getTotalAnimalList(): Call<ResponseAnimalListDto>  //매개변수 필요 시 괄호 안에 넣기

    /* 단일 동물 조회 */

    @GET("animals/{id}")
    fun getAnimalDetail(
        @Path("id") id: Int
    ): Call<ResponseAnimalDetailWrapper>

    //동물 저장
    @POST("animals")
    fun registerAnimal(@Body request: RequestAnimalDto): Call<Void>

    //동물 삭제
    @DELETE("animals/{id}")
    fun deleteAnimal(@Path("id")id:Int): Call<Void>

}