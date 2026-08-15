package com.konkuk.moru.data.service

import com.konkuk.moru.data.dto.response.Search.FavoriteTagAddRequest
import com.konkuk.moru.data.dto.response.Search.FavoriteTagItemResponse
import com.konkuk.moru.data.dto.response.Search.PageResponse
import com.konkuk.moru.data.dto.response.Search.RoutineSearchRequest
import com.konkuk.moru.data.dto.response.Search.RoutineSummaryResponse
import com.konkuk.moru.data.dto.response.Search.SearchHistoryResponse
import com.konkuk.moru.data.dto.response.Search.TagAllResponse
import retrofit2.http.*
import retrofit2.Response

interface SearchService {
    // ----------------------- 검색 기록 -----------------------
    @GET("/api/routines/search/history/routine-name")
    suspend fun getRoutineNameHistories(): List<SearchHistoryResponse>

    @DELETE("/api/routines/search/history/{historyId}")
    suspend fun deleteSearchHistory(@Path("historyId") historyId: String): Response<Unit>

    @DELETE("/api/routines/search/history/all")
    suspend fun deleteAllSearchHistory(): Response<Unit>

    // ----------------------- 자동완성 -----------------------
    @GET("/api/routines/search/suggestions/routine-title")
    suspend fun getTitleSuggestions(@Query("keyword") keyword: String): List<String>

    // ----------------------- 루틴 검색 -----------------------
    @POST("/api/routines/search")
    suspend fun searchRoutines(@Body body: RoutineSearchRequest): PageResponse<RoutineSummaryResponse>

    //-----------------------모든 태그 검색---------------------
    @GET("/api/tags")
    suspend fun getAllTags(): List<TagAllResponse>


    // ----------------------- 관심 태그 -----------------------
    @GET("/api/user/favorite-tag")
    suspend fun getFavoriteTags(): List<FavoriteTagItemResponse>

    @POST("/api/user/favorite-tag")
    suspend fun addFavoriteTags(@Body body: FavoriteTagAddRequest): Response<Unit>

    @DELETE("/api/user/favorite-tag/{tagId}")
    suspend fun deleteFavoriteTag(@Path("tagId") tagId: String): Response<Unit>
}
