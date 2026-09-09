package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Call direction. Gradle already blocks most of this, but these tests name the rule, so a violation
 * fails with the reason rather than "unresolved reference".
 */
class LayerDependencyTest {

    @Test
    fun `domain never imports data`() {
        SourceTree.module("domain").assertNoImportsOf("com.template.data")
    }

    @Test
    fun `domain never imports the UI lane`() {
        SourceTree.module("domain")
            .assertNoImportsOf("com.template.app", "com.template.design", "com.template.feature")
    }

    @Test
    fun `domain stays free of Compose and Android`() {
        SourceTree.module("domain")
            .assertNoImportsOf("androidx.", "org.jetbrains.compose", "android.")
    }

    @Test
    fun `data never imports the UI lane`() {
        SourceTree.module("data")
            .assertNoImportsOf("com.template.app", "com.template.design", "com.template.feature")
    }

    @Test
    fun `the UI lane never imports a data implementation`() {
        SourceTree.modules("app", "feature/settings").assertNoImportsOf("com.template.data")
    }

    @Test
    fun `a feature module never imports another feature`() {
        val violations = SourceTree.module("feature/settings")
            .flatMap { source ->
                source.imports
                    .filter { it.startsWith("com.template.feature.") }
                    .filterNot { it.startsWith("com.template.feature.settings") }
                    .map { "${source.path} imports $it" }
            }
        assertTrue(violations.isEmpty(), "Features must not depend on each other:\n${violations.pretty()}")
    }

    @Test
    fun `core never imports a layer above it`() {
        SourceTree.modules("core/common", "core/ui").assertNoImportsOf(
            "com.template.domain",
            "com.template.data",
            "com.template.app",
            "com.template.feature",
            "com.template.design",
        )
    }

    private fun List<KotlinSource>.assertNoImportsOf(vararg prefixes: String) {
        val violations = flatMap { source ->
            source.imports
                .filter { import -> prefixes.any(import::startsWith) }
                .map { "${source.path} imports $it" }
        }
        assertTrue(
            violations.isEmpty(),
            "Forbidden imports (${prefixes.joinToString()}):\n${violations.pretty()}",
        )
    }
}

internal fun List<String>.pretty(): String = joinToString("\n") { "  - $it" }
