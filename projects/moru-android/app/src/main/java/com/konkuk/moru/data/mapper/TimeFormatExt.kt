package com.konkuk.moru.data.mapper

// [추가] "HH:mm:ss" → "PTxHxMxS"
fun String?.toIso8601(): String? {
    if (this.isNullOrBlank()) return null
    val parts = this.split(":").mapNotNull { it.toIntOrNull() }
    val h = parts.getOrElse(0) { 0 }
    val m = parts.getOrElse(1) { 0 }
    val s = parts.getOrElse(2) { 0 }
    return buildString {
        append("PT")
        if (h > 0) append("${h}H")
        if (m > 0) append("${m}M")
        if (s > 0) append("${s}S")
        if (h == 0 && m == 0 && s == 0) append("0S")
    }
}

// [추가] "PT5M" → "HH:mm:ss"
fun String?.iso8601ToHms(): String {
    if (this.isNullOrBlank()) return "00:00:00"
    val src = this
    var h = 0; var m = 0; var s = 0
    try {
        // 매우 단순 파서 (H/M/S 중 일부만 있어도 처리)
        val body = src.removePrefix("PT")
        val hIdx = body.indexOf('H').takeIf { it >= 0 }?.let {
            val num = body.substring(0, it).toIntOrNull(); if (num != null) h = num; it + 1
        } ?: 0
        val rest1 = body.drop(hIdx)
        val mIdx = rest1.indexOf('M').takeIf { it >= 0 }?.let {
            val num = rest1.substring(0, it).filter { ch -> ch.isDigit() }.toIntOrNull()
            if (num != null) m = num; it + 1
        } ?: 0
        val rest2 = rest1.drop(mIdx)
        val sIdx = rest2.indexOf('S').takeIf { it >= 0 }?.let {
            val num = rest2.substring(0, it).filter { ch -> ch.isDigit() }.toIntOrNull()
            if (num != null) s = num; it + 1
        }
    } catch (_: Exception) { /* no-op */ }
    return "%02d:%02d:%02d".format(h, m, s)
}