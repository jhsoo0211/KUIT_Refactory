package com.konkuk.moru.data.repositoryimpl

import android.util.Log
import com.konkuk.moru.data.dto.response.OBImageUploadResponse
import com.konkuk.moru.data.service.CRImageService
import com.konkuk.moru.domain.repository.CRImageRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class CRImageRepositoryImpl @Inject constructor(
    private val service: CRImageService
) : CRImageRepository {

    override suspend fun uploadImage(file: File): Result<String> = runCatching {
        val mime = when (file.extension.lowercase()) {
            "jpg","jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            "gif" -> "image/gif"
            else -> "image/*"
        }
        val body = file.asRequestBody(mime.toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, body)

        Log.d("createroutine", "[upload] started")
        val res = service.uploadImage(part)
        Log.d("createroutine", "[upload] resp code=${res.code()}")

        if (!res.isSuccessful) {
            throw HttpException(res)
        }

        val obj: OBImageUploadResponse? = res.body()
        val key = obj?.bestKeyOrNull()
        require(!key.isNullOrBlank()) { "uploadImage response has no url/key" }

        Log.d("createroutine", "[upload] success")
        key
    }.onFailure { e ->
        Log.d(
            "createroutine",
            "[upload] failed: exception=${e::class.java.simpleName}"
        )
    }
}
