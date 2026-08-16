package com.konkuk.moru.domain.repository

import java.io.File

interface CRImageRepository {
    suspend fun uploadImage(file: File): Result<String>
}