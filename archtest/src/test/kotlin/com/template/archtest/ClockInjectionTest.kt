package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

class ClockInjectionTest {

    // This module's own sources quote the thing they ban.
    private val sources = SourceTree.all.filterNot { it.module == "archtest" }

    @Test
    fun `the system clock is read only where the clock is bound`() {
        val violations = sources
            .filter { source -> source.code().any { "Clock.System" in it } }
            .filterNot { it.path.contains("/di/") }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "Take a `Clock` in the constructor and call `clock.today()` — a class that reads " +
                "`Clock.System` cannot be pinned by a test:\n${violations.pretty()}",
        )
    }

    /** A rule about calls has to skip comments; naming the banned thing in one is the point of it. */
    private fun KotlinSource.code(): List<String> = text.lineSequence()
        .map { it.trim() }
        .filterNot { it.startsWith("//") || it.startsWith("*") || it.startsWith("/*") }
        .toList()
}
