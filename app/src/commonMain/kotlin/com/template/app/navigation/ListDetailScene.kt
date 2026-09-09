package com.template.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.template.design.theme.AppTheme

/**
 * The destination that keeps a pane of its own when the stack renders as list plus detail, or
 * `null` when the top of the stack owns the whole window.
 *
 * Nothing in the back stack changes with the window: a `Forecast` is pushed the same way at every
 * size, and only how the last two entries are *rendered* differs. That is what makes a resize
 * correct in both directions without the navigator knowing the window exists.
 */
fun listPaneOf(backStack: List<Destination>): Destination? {
    if (backStack.lastOrNull() !is Destination.Forecast) return null
    return backStack.getOrNull(backStack.lastIndex - 1)?.takeIf { NavTab.of(it) != null }
}

/**
 * `NavEntry.key` is private, so the strategy is handed the destination that pairs up rather than
 * inspecting entries for it: `NavDisplay` builds entries from the back stack in order, so the last
 * two entries are the last two destinations.
 */
data class ListDetailSceneStrategy(private val listPane: Destination?) : SceneStrategy<Destination> {

    override fun SceneStrategyScope<Destination>.calculateScene(
        entries: List<NavEntry<Destination>>,
    ): Scene<Destination>? = when {
        listPane == null || entries.size < 2 -> null
        else -> ListDetailScene(
            key = listPane,
            list = entries[entries.lastIndex - 1],
            detail = entries.last(),
            previousEntries = entries.dropLast(1),
        )
    }
}

/** Keyed by the list destination, so swapping the detail does not re-animate both panes. */
private data class ListDetailScene(
    override val key: Any,
    val list: NavEntry<Destination>,
    val detail: NavEntry<Destination>,
    override val previousEntries: List<NavEntry<Destination>>,
) : Scene<Destination> {

    override val entries: List<NavEntry<Destination>> = listOf(list, detail)

    override val content: @Composable () -> Unit = {
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.width(AppTheme.sizing.listPaneWidth).fillMaxHeight()) {
                list.Content()
            }
            Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.TopCenter) {
                Box(Modifier.widthIn(max = AppTheme.sizing.contentMaxWidth)) {
                    detail.Content()
                }
            }
        }
    }
}
