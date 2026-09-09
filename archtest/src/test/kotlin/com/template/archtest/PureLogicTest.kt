package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * The one property neither a name nor a folder reveals. `logic/` holds both pure logic and
 * UseCases; what separates them is the signature, so these tests police the things purity rules
 * out for *everything* in the tier — no clock, no randomness, no platform.
 */
class PureLogicTest {

    private val logic = SourceTree.module("domain").filter { it.path.contains("/logic/") }

    @Test
    fun `there is logic to check`() {
        assertTrue(logic.isNotEmpty(), "Found no domain logic — has the layout changed?")
    }

    @Test
    fun `logic never reads a clock`() {
        val violations = logic
            .filter { it.text.contains("Clock.System") || it.imports.any { i -> i == "kotlin.time.Clock" } }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "Take the current time as a parameter — a rule that reads a clock cannot be " +
                "pinned by a test:\n${violations.pretty()}",
        )
    }

    @Test
    fun `logic never uses randomness`() {
        val violations = logic.filter { it.text.contains("Random") }.map { it.path }
        assertTrue(violations.isEmpty(), "Pass the value in:\n${violations.pretty()}")
    }

    @Test
    fun `logic has no platform dependencies`() {
        val violations = logic
            .flatMap { source ->
                source.imports
                    .filter { it.startsWith("androidx.") || it.startsWith("android.") || it.startsWith("platform.") }
                    .map { "${source.path} imports $it" }
            }
        assertTrue(violations.isEmpty(), "Domain logic is pure Kotlin:\n${violations.pretty()}")
    }
}
