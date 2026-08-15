package com.konkuk.moru.core.util.modifier

fun String?.toIsoDurationOrZero(): String {
    val v = this?.trim().orEmpty()
    if (v.isEmpty()) return "PT0S"
    if (v.uppercase().startsWith("PT")) return v
    return try {
        val p = v.split(":").map { it.toInt() }
        when (p.size) {
            3 -> "PT${p[0]}H${p[1]}M${p[2]}S"
            2 -> "PT${p[0]}M${p[1]}S"
            else -> "PT0S"
        }
    } catch (_: Exception) {
        "PT0S"
    }
}