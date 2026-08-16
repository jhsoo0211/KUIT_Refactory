package com.konkuk.moru.core.util

/**
 * Converts legacy numeric or textual server IDs to the stable key required by older Int APIs.
 *
 * This is a compatibility key, not a globally unique identifier. Callers must retain the
 * original String ID for navigation and network requests.
 */
fun String.toStableIntId(): Int {
    this.toLongOrNull()?.let {
        val mod = (it % Int.MAX_VALUE).toInt()
        return if (mod >= 0) mod else -mod
    }
    var h = 0
    for (ch in this) h = (h * 31) + ch.code
    return h
}
