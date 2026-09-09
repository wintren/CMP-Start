package com.template.design.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Fixed sizes that are not spacing: icon boxes and stroke widths. */
data class AppSizing(
    /** Sits inline with text, in a button or a chip. */
    val iconInline: Dp = 18.dp,
    /** Leading or trailing in a dense row. */
    val iconSmall: Dp = 20.dp,
    /** The default: a list row, a top bar action. */
    val icon: Dp = 24.dp,
    /** A row that is the primary content, not a decoration. */
    val iconLarge: Dp = 28.dp,
    /** The single illustration in an empty or error state. */
    val iconDisplay: Dp = 44.dp,
    val border: Dp = 1.dp,
    val borderFocused: Dp = 2.dp,
)
