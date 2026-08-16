package com.konkuk.moru.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class IdExtensionsTest {

    @Test
    fun `numeric IDs preserve small positive values`() {
        assertEquals(42, "42".toStableIntId())
    }

    @Test
    fun `negative numeric IDs become non-negative compatibility keys`() {
        assertEquals(42, "-42".toStableIntId())
    }

    @Test
    fun `large numeric IDs are reduced deterministically`() {
        val id = Long.MAX_VALUE.toString()
        val expected = (Long.MAX_VALUE % Int.MAX_VALUE).toInt()

        assertEquals(expected, id.toStableIntId())
        assertEquals(expected, id.toStableIntId())
    }

    @Test
    fun `text IDs produce the same key on repeated conversion`() {
        val id = "550e8400-e29b-41d4-a716-446655440000"

        assertEquals(id.toStableIntId(), id.toStableIntId())
    }
}
