package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

class FileLayoutTest {

    /**
     * Git tracks files: splitting a two-type file later creates a *new* file and loses the moved
     * type's history.
     */
    @Test
    fun `a file declaring several top-level types is named after one of them`() {
        val exempt = Regex(""".*(Screen|Models|Theme|Ext|Views|Format|Label|Mapper|Contracts|Tuple|Combines|DI|Modules|SourceTree)\.kt$""")
        val violations = SourceTree.all
            .asSequence()
            .filterNot { it.path.contains("/commonTest/") || it.path.contains("/src/test/") }
            .filterNot { exempt.matches(it.fileName) }
            .filter { it.topLevelTypeNames.size > 1 }
            .filterNot { source ->
                source.fileName.removeSuffix(".kt") in source.topLevelTypeNames
            }
            .map { "${it.path} declares ${it.topLevelTypeNames}" }
            .toList()
        assertTrue(violations.isEmpty(), "Name the file after its type:\n${violations.pretty()}")
    }

    @Test
    fun `no module keeps a central di package`() {
        val violations = SourceTree.modules("domain", "data")
            .filter { it.path.contains("/di/") }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "DI lives with the area it wires (`<area>/<Area>DomainDI.kt`), so you edit bindings " +
                "in the folder you are already in:\n${violations.pretty()}",
        )
    }
}
