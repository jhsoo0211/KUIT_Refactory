package com.konkuk.moru.data.net

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.konkuk.moru.data.dto.error.ErrorEnvelope
import okhttp3.ResponseBody

object ErrorBodyParser {
    private const val TAG = "createroutine"

    fun parse(body: ResponseBody?): ErrorEnvelope? {
        if (body == null) return null
        return try {
            val raw = body.string() // ⚠️ 한번만 읽을 수 있음

            val json = JsonParser.parseString(raw).asJsonObject

            // 1차: 커스텀 스키마(status, code, message) 시도
            val custom = extractCustom(json)
            if (custom != null) return custom

            // 2차: 기본 스프링(timestamp, status, error, path) 시도
            val basic = extractSpring(json)
            basic
        } catch (e: Exception) {
            Log.d(TAG, "[err] parse failed: exception=${e::class.java.simpleName}")
            null
        }
    }

    private fun extractCustom(o: JsonObject): ErrorEnvelope? {
        val hasStatus = o.has("status")
        val hasCode = o.has("code")
        val hasMessage = o.has("message")
        if (hasStatus || hasCode || hasMessage) {
            return ErrorEnvelope(
                status = o.getAsIntOrNull("status"),
                code = o.getAsStringOrNull("code"),
                message = o.getAsStringOrNull("message")
            )
        }
        return null
    }

    private fun extractSpring(o: JsonObject): ErrorEnvelope? {
        val hasTimestamp = o.has("timestamp")
        val hasStatus = o.has("status")
        val hasError = o.has("error")
        val hasPath = o.has("path")
        if (hasTimestamp || hasStatus || hasError || hasPath) {
            return ErrorEnvelope(
                timestamp = o.getAsStringOrNull("timestamp"),
                status = o.getAsIntOrNull("status"),
                error = o.getAsStringOrNull("error"),
                path = o.getAsStringOrNull("path")
            )
        }
        return null
    }

    private fun JsonObject.getAsStringOrNull(name: String): String? =
        if (this.has(name) && !this.get(name).isJsonNull) this.get(name).asString else null

    private fun JsonObject.getAsIntOrNull(name: String): Int? =
        if (this.has(name) && !this.get(name).isJsonNull) this.get(name).asInt else null
}
