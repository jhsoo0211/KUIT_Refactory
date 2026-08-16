package com.konkuk.moru.core.util

import java.time.*
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

fun String?.toEpochMsOrZero(): Long {
    if (this.isNullOrBlank()) return 0L
    return try {
        Instant.parse(this).toEpochMilli()
    } catch (_: Exception) {
        try {
            val f = DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                .optionalEnd()
                .toFormatter()
            LocalDateTime.parse(this, f).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } catch (_: Exception) { 0L }
    }
}