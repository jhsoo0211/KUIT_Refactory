package com.konkuk.moru.core.util


import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateFormatters {
    private val out = DateTimeFormatter.ofPattern("MM.dd.")
    fun isoToMonthDayDot(iso: String): String =
        runCatching { OffsetDateTime.parse(iso).format(out) }.getOrElse { "" }
}