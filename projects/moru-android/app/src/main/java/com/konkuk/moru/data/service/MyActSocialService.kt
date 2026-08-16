package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.MyActScrapsPageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MyActSocialService {
    @GET("api/social/scraps")
    suspend fun getScraps(
        @Query("createdAt") createdAt: String? = null,
        @Query("scrapId") scrapId: String? = null,
        @Query("size") size: Int? = null
    ): MyActScrapsPageResponse
}