package com.template.archtest

import java.io.File

/**
 * Deliberately not Konsist or any rule framework: this must survive every Kotlin, AGP and KMP
 * upgrade, and it has to read `commonMain` of modules it must not depend on.
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

    val repoRoot: File = File(System.getProperty("repoRoot") ?: ".")

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
