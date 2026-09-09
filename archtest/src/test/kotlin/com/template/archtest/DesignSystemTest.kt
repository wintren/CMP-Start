package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

class DesignSystemTest {

    private val paletteDirectory = "design/src/commonMain/kotlin/com/template/design/theme/"

    private val designComponents = SourceTree.module("design")
        .filter { it.path.contains("/design/component/") }

    @Test
    fun `there are design components to check`() {
        assertTrue(designComponents.isNotEmpty(), "Found no design components — has the layout changed?")
    }

    /** A hex literal outside the palette is a colour that survives a re-theme. */
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

    /** A bare `Text` means a `String` in state, which is a screen that cannot be translated. */
    @Test
    fun `only the design system calls Text directly`() {
        val violations = SourceTree.all
            .filterNot { it.module == "design" }
            .filter { source -> source.imports.any { it == "androidx.compose.material3.Text" } }
            .map { it.path }
        assertTrue(violations.isEmpty(), "Use `AppText`:\n${violations.pretty()}")
    }

    /** Screens only. A component may hold a literal `dp` — a chip's 2dp inset is local to it. */
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
     * The window is measured once, in `AppTheme`. A layout reads `AppTheme.windowSize`, or
     * `AppWindowSize.of(maxWidth)` when what matters is the width of its own pane.
     */
    @Test
    fun `only the theme measures the window`() {
        val violations = SourceTree.all
            .filterNot { it.path.startsWith(paletteDirectory) }
            .filter { source -> source.imports.any { it == "androidx.compose.ui.platform.LocalWindowInfo" } }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "Read `AppTheme.windowSize`, or `AppWindowSize.of(maxWidth)` inside a " +
                "BoxWithConstraints:\n${violations.pretty()}",
        )
    }

    /** A component nobody can see is a component nobody maintains. */
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
