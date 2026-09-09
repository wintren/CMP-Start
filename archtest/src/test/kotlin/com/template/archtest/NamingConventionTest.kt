package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

class NamingConventionTest {

    @Test
    fun `Service is not a name we use`() {
        val violations = SourceTree.modules("domain", "data", "app", "feature/settings")
            .flatMap { source ->
                source.topLevelTypeNames
                    .filter { it.endsWith("Service") }
                    .map { "${source.path} declares $it" }
            }
        assertTrue(
            violations.isEmpty(),
            "Every role `Service` used to blur has a precise name — Repository, UseCase, " +
                "Source, Client, logic. See docs/architecture.md.\n${violations.pretty()}",
        )
    }

    @Test
    fun `repository implementations live in data or in the feature that owns them`() {
        val violations = SourceTree.all
            .filter { it.fileName.endsWith("RepositoryImpl.kt") }
            .filterNot { it.module == "data" || it.module.startsWith("feature/") }
            .map { it.path }
        assertTrue(violations.isEmpty(), "Misplaced repository impls:\n${violations.pretty()}")
    }

    @Test
    fun `repository and provider implementations are internal`() {
        val violations = SourceTree.all
            .filter { it.fileName.endsWith("RepositoryImpl.kt") || it.fileName.endsWith("ProviderImpl.kt") }
            .filterNot { source -> source.text.contains(Regex("""^internal class""", RegexOption.MULTILINE)) }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "An implementation must be `internal` so nothing can depend on it and it stays off " +
                "the iOS export surface:\n${violations.pretty()}",
        )
    }

    @Test
    fun `wire types never leave the data module`() {
        val violations = SourceTree.all
            .filterNot { it.module == "data" }
            .flatMap { source ->
                source.imports
                    .filter { it.contains(".source.") || it.endsWith("Dto") || it.contains(".dto.") }
                    .map { "${source.path} imports $it" }
            }
        assertTrue(
            violations.isEmpty(),
            "DTOs and Sources are `:data`'s business; the repository maps them to domain " +
                "models:\n${violations.pretty()}",
        )
    }
}
