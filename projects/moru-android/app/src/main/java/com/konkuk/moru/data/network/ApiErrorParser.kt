package com.konkuk.moru.data.network

import com.google.gson.Gson
import com.konkuk.moru.data.dto.response.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Response

object ApiErrorParser {
    private val gson = Gson()

    fun parse(response: Response<*>): Pair<String?, ErrorResponse?> {
        val body: ResponseBody = response.errorBody() ?: return null to null
        val raw = try { body.string() } catch (_: Exception) { null }
        val parsed = try { raw?.let { gson.fromJson(it, ErrorResponse::class.java) } } catch (_: Exception) { null }
        return raw to parsed
    }
}