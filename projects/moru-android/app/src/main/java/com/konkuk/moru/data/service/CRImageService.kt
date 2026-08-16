package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.OBImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CRImageService {
    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<OBImageUploadResponse>
}