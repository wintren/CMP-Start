package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * The design system is only "the only design system" if nothing routes around it. These are the
 * three ways it gets routed around, plus the rule that keeps a component visible.
 */
class DesignSystemTest {

    private val paletteDirectory = "design/src/commonMain/kotlin/com/template/design/theme/"

    private val designComponents = SourceTree.module("design")
        .filter { it.path.contains("/design/component/") }

    @Test
    fun `there are design components to check`() {
        assertTrue(designComponents.isNotEmpty(), "Found no design components — has the layout changed?")
    }

    /**
     * A hex literal outside the palette is a colour that survives a re-theme, which is exactly the
     * bug `AppColors` exists to make impossible.
     */
    @Test
    fun `colours come from the palette, not from hex literals`() {
        val violations = SourceTree.all
            .filterNot { it.path.startsWith(paletteDirectory) }
            .filterNot { it.path.contains("/commonTest/") || it.path.contains("/src/test/") }
            .filter { it.text.contains(Regex("""Color\(0x""")) }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "Add a role to AppColors and read it through `AppTheme.colors`:\n${violations.pretty()}",
        )
    }

    /**
     * `AppText` takes a `StringValue`, so a bare `Text` is also a `String` in state — the thing
     * that makes a screen untranslatable and a state assertion compare English.
     */
    @Test
    fun `only the design system calls Text directly`() {
        val violations = SourceTree.all
            .filterNot { it.module == "design" }
            .filter { source -> source.imports.any { it == "androidx.compose.material3.Text" } }
            .map { it.path }
        assertTrue(violations.isEmpty(), "Use `AppText`:\n${violations.pretty()}")
    }

    /**
     * Screens only. A component may hold a literal `dp` — a chip's 2dp inset is genuinely local to
     * it and a token for it would be noise. A screen laying out its own icon sizes is how two
     * lists end up a hair apart, so screens take every measurement from `AppTheme`.
     */
    @Test
    fun `screens take their measurements from the theme`() {
        val violations = SourceTree.all
            .filter { it.fileName.endsWith("Screen.kt") }
            .flatMap { source ->
                DP_LITERAL.findAll(source.text).map { "${source.path}: ${it.value.trim()}" }
            }
        assertTrue(
            violations.isEmpty(),
            "Use `AppTheme.spacing`, `AppTheme.sizing` or add a role to one of them:\n" +
                violations.pretty(),
        )
    }

    /**
     * A component nobody can see is a component nobody maintains. The preview is also the cheapest
     * proof it renders outside the one screen it was written for.
     */
    @Test
    fun `every design component carries a preview`() {
        val violations = designComponents
            .filterNot { it.text.contains("@Preview") }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "Add `@Preview` wrapped in `AppPreview { }`, and a `…Showcase()` the catalog can " +
                "reuse — see AppButton.kt:\n${violations.pretty()}",
        )
    }

    /** The showcase is what `AppCatalog` renders; a preview-only component is invisible there. */
    @Test
    fun `every design component exposes a showcase for the catalog`() {
        val violations = designComponents
            .filterNot { it.text.contains(Regex("""fun \w+Showcase\(""")) }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "Name the preview body `<Component>Showcase()` and call it from " +
                "app/catalog/AppCatalog.kt:\n${violations.pretty()}",
        )
    }

    private companion object {
        val DP_LITERAL = Regex("""(?<![\w.])\d+(\.\d+)?\.dp\b""")
    }
}
