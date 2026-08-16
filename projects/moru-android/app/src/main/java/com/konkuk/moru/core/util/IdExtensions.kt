package com.konkuk.moru.core.util

/** 서버의 String ID(UUID/숫자)를 안정적인 Int 키로 변환 (항상 동일 문자열 → 동일 Int) */
fun String.toStableIntId(): Int {
    this.toLongOrNull()?.let {
        val mod = (it % Int.MAX_VALUE).toInt()
        return if (mod >= 0) mod else -mod
    }
    var h = 0
    for (ch in this) h = (h * 31) + ch.code
    return h
}
