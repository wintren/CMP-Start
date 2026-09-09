package com.template.design.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Ordered, so `AppTheme.windowSize >= AppWindowSize.Medium` reads as "at least this wide".
 *
 * [of] takes any width because a pane has to ask the question about itself: the window is
 * `Expanded` while the 360dp list pane inside it is `Compact`, and a layout that reads the window
 * instead of its own width puts two columns in a phone-width pane.
 */
enum class AppWindowSize {
    Compact,
    Medium,
    Expanded,
    ;

    companion object {
        val mediumFrom: Dp = 600.dp
        val expandedFrom: Dp = 840.dp

        fun of(width: Dp): AppWindowSize = when {
            width < mediumFrom -> Compact
            width < expandedFrom -> Medium
            else -> Expanded
        }
    }
}
