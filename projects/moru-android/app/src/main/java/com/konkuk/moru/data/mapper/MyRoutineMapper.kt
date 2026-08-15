package com.konkuk.moru.data.mapper

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// [추가] 요일 → 서버 문자열
fun DayOfWeek.toMyApiString(): String = when (this) {
    DayOfWeek.MONDAY -> "MON"
    DayOfWeek.TUESDAY -> "TUE"
    DayOfWeek.WEDNESDAY -> "WED"
    DayOfWeek.THURSDAY -> "THU"
    DayOfWeek.FRIDAY -> "FRI"
    DayOfWeek.SATURDAY -> "SAT"
    DayOfWeek.SUNDAY -> "SUN"
}

fun String?.toMyMinutesOrNull(): Long? = try {
    this?.let { Duration.parse(it).toMinutes() }
} catch (_: Exception) {
    null
}

// 시간 파싱
fun String.toMyLocalTimeOrNull(): LocalTime? {
    val s = trim()
    return runCatching { LocalTime.parse(s) }.getOrNull() // ISO 기본 (HH:mm[:ss][.SSS]도 커버)
        ?: runCatching { LocalTime.parse(s.take(8)) }.getOrNull() // "10:06:00xxxxx" 같은 보호
        ?: runCatching { LocalTime.parse(s, DateTimeFormatter.ofPattern("H:mm")) }.getOrNull()
}

// 요일 파싱 (3글자, 풀네임 모두)
fun String?.toDayOfWeekOrNull(): DayOfWeek? {
    if (this.isNullOrBlank()) return null
    return when (this.uppercase()) {
        "MON", "MONDAY" -> DayOfWeek.MONDAY
        "TUE", "TUESDAY" -> DayOfWeek.TUESDAY
        "WED", "WEDNESDAY" -> DayOfWeek.WEDNESDAY
        "THU", "THURSDAY" -> DayOfWeek.THURSDAY
        "FRI", "FRIDAY" -> DayOfWeek.FRIDAY
        "SAT", "SATURDAY" -> DayOfWeek.SATURDAY
        "SUN", "SUNDAY" -> DayOfWeek.SUNDAY
        else -> null
    }
}