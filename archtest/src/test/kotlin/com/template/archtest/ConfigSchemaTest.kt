package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * `AppConfig` is generated from whichever `config/<env>.properties` the build selected, so a key
 * added to one file and not the others compiles in `dev` and fails to compile in `prod`.
 */
class ConfigSchemaTest {

    private val files = SourceTree.repoRoot.resolve("config")
        .listFiles { file -> file.extension == "properties" }
        .orEmpty()
        .sortedBy { it.name }

    private fun keys(text: String): Set<String> = text.lineSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() && !it.startsWith("#") && it.contains("=") }
        .map { it.substringBefore("=").trim() }
        .toSet()

    @Test
    fun `there are environments to check`() {
        assertTrue(files.size > 1, "Found fewer than two config/*.properties — has the layout changed?")
    }

    @Test
    fun `every environment declares the same keys`() {
        val schemas = files.associate { it.name to keys(it.readText()) }
        val union = schemas.values.flatten().toSet()
        val violations = schemas
            .filterValues { it != union }
            .map { (name, declared) -> "config/$name is missing ${(union - declared).sorted()}" }
        assertTrue(
            violations.isEmpty(),
            "A key exists in every environment or in none — otherwise `AppConfig.<key>` compiles " +
                "for one `-Penv` and not another:\n${violations.pretty()}",
        )
    }
}
