package com.konkuk.moru.presentation.navigation

import java.nio.charset.StandardCharsets

/**
 * Converts the notification payload formats MORU owns into an app route.
 *
 * Payload IDs are always treated as raw values and encoded exactly once. A canonical route is
 * accepted only when it belongs to the notification allow-list; otherwise the resolver falls back
 * to the raw FCM/legacy ID instead of navigating to an arbitrary screen.
 */
object NotificationRouteResolver {
    const val ROUTE_EXTRA_KEY = "com.konkuk.moru.extra.NOTIFICATION_ROUTE"
    const val ROUTINE_ID_KEY = "routineId"
    const val LEGACY_ROUTINE_ID_KEY = "ROUTINE_ID"

    fun resolve(payload: Map<String, String?>): String? {
        supportedCanonicalRoute(payload[ROUTE_EXTRA_KEY])?.let { return it }

        val routineId = sequenceOf(
            payload[ROUTINE_ID_KEY],
            payload[LEGACY_ROUTINE_ID_KEY]
        ).mapNotNull { it?.trim()?.takeIf(String::isNotEmpty) }
            .firstOrNull()
            ?: return null

        return CanonicalRoutes.routineFeedDetail(routineId)
    }

    private fun supportedCanonicalRoute(candidate: String?): String? {
        val route = candidate?.trim()?.takeIf(String::isNotEmpty) ?: return null
        val encodedId = route.removePrefix(CanonicalRoutes.ROUTINE_FEED_DETAIL_PREFIX)
        if (encodedId == route || !encodedId.isCanonicalPathSegment()) return null
        return route
    }
}

internal object CanonicalRoutes {
    private const val ROUTINE_FEED_DETAIL_BASE = "routine_feed_detail"
    const val ROUTINE_ID_ARGUMENT = "routineId"
    const val ROUTINE_FEED_DETAIL_PATTERN =
        "$ROUTINE_FEED_DETAIL_BASE/{$ROUTINE_ID_ARGUMENT}"
    const val ROUTINE_FEED_DETAIL_PREFIX = "$ROUTINE_FEED_DETAIL_BASE/"

    fun routineFeedDetail(routineId: String): String =
        "$ROUTINE_FEED_DETAIL_PREFIX${routineId.encodeAsPathSegment()}"
}

/** RFC 3986 unreserved bytes stay readable; every other UTF-8 byte is percent-encoded. */
private fun String.encodeAsPathSegment(): String = buildString {
    this@encodeAsPathSegment.toByteArray(StandardCharsets.UTF_8).forEach { byte ->
        val value = byte.toInt() and 0xff
        if (value.isUnreservedByte()) {
            append(value.toChar())
        } else {
            append('%')
            append(HEX_DIGITS[value ushr 4])
            append(HEX_DIGITS[value and 0x0f])
        }
    }
}

private fun String.isCanonicalPathSegment(): Boolean {
    if (isEmpty()) return false

    var index = 0
    while (index < length) {
        val character = this[index]
        when {
            character.code.isUnreservedByte() -> index += 1
            character == '%' &&
                index + 2 < length &&
                this[index + 1] in HEX_DIGITS &&
                this[index + 2] in HEX_DIGITS -> index += 3
            else -> return false
        }
    }
    return true
}

private fun Int.isUnreservedByte(): Boolean =
    this in 'a'.code..'z'.code ||
        this in 'A'.code..'Z'.code ||
        this in '0'.code..'9'.code ||
        this == '-'.code ||
        this == '.'.code ||
        this == '_'.code ||
        this == '~'.code

private const val HEX_DIGITS = "0123456789ABCDEF"
