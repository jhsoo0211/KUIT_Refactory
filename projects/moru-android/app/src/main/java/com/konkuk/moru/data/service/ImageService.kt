package com.konkuk.moru.data.service

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// [추가] 서버가 Map<String,String>을 돌려주는 스웨거 예시 대응
interface ImageService {
    @Multipart
    @POST("api/images/upload") // baseUrl 뒤에 상대경로
    suspend fun upload(
        @Part file: MultipartBody.Part
    ): Map<String, String> // 서버가 {"imageUrl": "..."} 형태 Map 반환
}