package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.OBImageUploadResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface ImageUploadService {
    @Multipart
    @POST
    suspend fun uploadDynamic(
        @Url url: String,
        @Part file: MultipartBody.Part
    ): Response<OBImageUploadResponse>
}