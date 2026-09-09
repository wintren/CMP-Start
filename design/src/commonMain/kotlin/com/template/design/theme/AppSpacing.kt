package com.template.design.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** A 4dp scale. Screens use these instead of literal `dp`, so density decisions stay in one file. */
data class AppSpacing(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
)
