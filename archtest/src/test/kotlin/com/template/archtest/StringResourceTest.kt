package com.template.archtest

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class StringResourceTest {

    /**
     * Compose Resources falls back to the default locale silently, so a missing key looks like a
     * working app to anyone who does not read that language.
     * There is deliberately no rule about `StringValue.Raw` — whether a given `Raw` should have
     * been a key is a judgement a text scan cannot make.
     */
    @Test
    fun `every string key exists in every locale`() {
        val bundles = REPO_ROOT.walkTopDown()
            .onEnter { it.name != "build" }
            .filter { it.isFile && it.name == "strings.xml" }
            .groupBy { it.parentFile.parentFile }        // …/composeResources
            .toSortedMap(compareBy { it.invariantSeparatorsPath })

        assertTrue(bundles.isNotEmpty(), "Found no strings.xml — has the layout changed?")

        val violations = bundles.flatMap { (bundleRoot, files) ->
            val byLocale = files.associate { it.parentFile.name to it.keys() }
            val default = byLocale["values"]
                ?: return@flatMap listOf("${bundleRoot.relativeTo(REPO_ROOT)}: no values/strings.xml")

            byLocale.filterKeys { it != "values" }.flatMap { (locale, keys) ->
                val module = bundleRoot.relativeTo(REPO_ROOT).invariantSeparatorsPath
                (default - keys).map { "$module/$locale is missing '$it'" } +
                    (keys - default).map { "$module/$locale has '$it', absent from values/" }
            }
        }
        assertTrue(
            violations.isEmpty(),
            "A new key goes into every locale in the same commit:\n${violations.pretty()}",
        )
    }

    private fun File.keys(): Set<String> =
        KEY.findAll(readText()).map { it.groupValues[1] }.toSet()

    private companion object {
        val REPO_ROOT = File(System.getProperty("repoRoot") ?: ".")

        val KEY = Regex("""<string\s+name="([^"]+)"""")
    }
}
