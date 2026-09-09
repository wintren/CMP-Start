package com.template.design.theme

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppWindowSizeTest {

    @Test
    fun `each breakpoint includes its lower bound and excludes the next`() {
        assertEquals(AppWindowSize.Compact, AppWindowSize.of(0.dp))
        assertEquals(AppWindowSize.Compact, AppWindowSize.of(599.dp))
        assertEquals(AppWindowSize.Medium, AppWindowSize.of(600.dp))
        assertEquals(AppWindowSize.Medium, AppWindowSize.of(839.dp))
        assertEquals(AppWindowSize.Expanded, AppWindowSize.of(840.dp))
    }

    @Test
    fun `the order is the comparison call sites rely on`() {
        assertTrue(AppWindowSize.Expanded > AppWindowSize.Medium)
        assertTrue(AppWindowSize.Medium > AppWindowSize.Compact)
    }
}
