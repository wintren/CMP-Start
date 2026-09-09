package com.template.archtest

import java.io.File

/**
 * Reads the repo's Kotlin sources as text.
 *
 * Deliberately not Konsist or any other rule framework: this must keep working across Kotlin, AGP
 * and KMP upgrades, and it has to see `commonMain` of modules this one must not depend on. Text is
 * a coarse tool, but a rule you can read in ten lines is a rule people keep.
 */
data class KotlinSource(
    val module: String,
    val path: String,
    val fileName: String,
    val text: String,
) {
    val imports: List<String> = text.lineSequence()
        .filter { it.startsWith("import ") }
        .map { it.removePrefix("import ").substringBefore(" as ").trim() }
        .toList()

    val topLevelTypeNames: List<String> = TYPE_REGEX.findAll(text)
        .map { it.groupValues[1] }
        .toList()

    override fun toString() = path

    private companion object {
        // Top-level only: a declaration at column zero. Nested types are indented.
        val TYPE_REGEX =
            Regex("""^(?:@\w+\s+)*(?:public |internal |private )?(?:expect |actual )?(?:abstract |open |sealed |data |value |enum |annotation |fun )*(?:class|interface|object)\s+(\w+)""", RegexOption.MULTILINE)
    }
}

object SourceTree {

    private val repoRoot: File = File(System.getProperty("repoRoot") ?: ".")

    private val ignoredDirectories = setOf("build", ".gradle", ".git", ".kotlin", "build-logic")

    val all: List<KotlinSource> by lazy { read() }

    fun module(name: String): List<KotlinSource> = all.filter { it.module == name }

    fun modules(vararg names: String): List<KotlinSource> =
        all.filter { it.module in names.toSet() }

    private fun read(): List<KotlinSource> = repoRoot
        .walkTopDown()
        .onEnter { it.name !in ignoredDirectories }
        .filter { it.isFile && it.extension == "kt" }
        .map { file ->
            val relative = file.relativeTo(repoRoot).invariantSeparatorsPath
            KotlinSource(
                module = relative.substringBefore("/src/"),
                path = relative,
                fileName = file.name,
                text = file.readText(),
            )
        }
        .toList()
        .also { require(it.isNotEmpty()) { "No Kotlin sources found under $repoRoot" } }
}
