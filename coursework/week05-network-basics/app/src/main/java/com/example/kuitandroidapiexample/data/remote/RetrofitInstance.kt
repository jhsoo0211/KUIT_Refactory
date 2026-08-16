package com.example.kuitandroidapiexample.data.remote

import com.example.kuitandroidapiexample.BuildConfig
import com.example.kuitandroidapiexample.data.service.HomeService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
//import retrofit2.converter.kotlinx.serialization.asConverterFactory




object RetrofitInstance {
    private val contentType = "application/json".toMediaType()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL) // ← local.properties에 base.url 반드시 확인!
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    val api: HomeService = retrofit.create(HomeService::class.java)
}
