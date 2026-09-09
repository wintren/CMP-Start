package com.template.archtest

import kotlin.test.Test
import kotlin.test.assertTrue

class ViewModelConventionTest {

    private val viewModels = SourceTree.all.filter {
        it.fileName.endsWith("ViewModel.kt") && !it.path.contains("/core/ui/")
    }

    @Test
    fun `there are view models to check`() {
        assertTrue(viewModels.isNotEmpty(), "Found no ViewModels — has the layout changed?")
    }

    @Test
    fun `every view model derives its state through viewModelState`() {
        val violations = viewModels
            .filterNot { it.text.contains("viewModelState(") }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "`stateFlow` must be built with `viewModelState`, never assigned from a " +
                "MutableStateFlow:\n${violations.pretty()}",
        )
    }

    @Test
    fun `no view model exposes a mutable state flow`() {
        val violations = viewModels
            .filter { source ->
                source.text.contains(Regex("""^\s*(?:override\s+)?val\s+stateFlow\s*(?::\s*Mutable|=\s*Mutable)""", RegexOption.MULTILINE))
            }
            .map { it.path }
        assertTrue(violations.isEmpty(), "State must be derived, not mutable:\n${violations.pretty()}")
    }

    @Test
    fun `screens receive state and actions, never a view model`() {
        val violations = SourceTree.all
            .filter { it.fileName.endsWith("Screen.kt") }
            .filter { it.text.contains(Regex(""":\s*\w*ViewModel""")) }
            .map { it.path }
        assertTrue(
            violations.isEmpty(),
            "A Screen takes `state` and `onAction` so it stays previewable:\n${violations.pretty()}",
        )
    }

    @Test
    fun `screens do not reach for a repository or use case`() {
        val violations = SourceTree.all
            .filter { it.fileName.endsWith("Screen.kt") }
            .flatMap { source ->
                source.imports
                    .filter { it.contains(".contract.") || it.contains(".logic.") }
                    .map { "${source.path} imports $it" }
            }
        assertTrue(violations.isEmpty(), "Composables render state:\n${violations.pretty()}")
    }
}
