package com.konkuk.moru.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationRouteResolverTest {

    @Test
    fun `routine payload resolves to the canonical detail route`() {
        val route = NotificationRouteResolver.resolve(
            mapOf(NotificationRouteResolver.ROUTINE_ID_KEY to "routine-42")
        )

        assertEquals("routine_feed_detail/routine-42", route)
    }

    @Test
    fun `reserved and unicode ID bytes are encoded exactly once`() {
        val route = NotificationRouteResolver.resolve(
            mapOf(NotificationRouteResolver.ROUTINE_ID_KEY to "morning/루틴%1")
        )

        assertEquals(
            "routine_feed_detail/morning%2F%EB%A3%A8%ED%8B%B4%251",
            route
        )
    }

    @Test
    fun `legacy activity extra remains supported`() {
        val route = NotificationRouteResolver.resolve(
            mapOf(NotificationRouteResolver.LEGACY_ROUTINE_ID_KEY to "legacy-id")
        )

        assertEquals("routine_feed_detail/legacy-id", route)
    }

    @Test
    fun `allow-listed canonical route wins over raw fallback`() {
        val route = NotificationRouteResolver.resolve(
            mapOf(
                NotificationRouteResolver.ROUTE_EXTRA_KEY to
                    "routine_feed_detail/already%2Fencoded",
                NotificationRouteResolver.ROUTINE_ID_KEY to "fallback"
            )
        )

        assertEquals("routine_feed_detail/already%2Fencoded", route)
    }

    @Test
    fun `unsupported route falls back to a recognized raw ID`() {
        val route = NotificationRouteResolver.resolve(
            mapOf(
                NotificationRouteResolver.ROUTE_EXTRA_KEY to "act_setting",
                NotificationRouteResolver.ROUTINE_ID_KEY to "fallback"
            )
        )

        assertEquals("routine_feed_detail/fallback", route)
    }

    @Test
    fun `missing or blank IDs do not navigate`() {
        assertNull(NotificationRouteResolver.resolve(emptyMap()))
        assertNull(
            NotificationRouteResolver.resolve(
                mapOf(NotificationRouteResolver.ROUTINE_ID_KEY to "   ")
            )
        )
    }
}
