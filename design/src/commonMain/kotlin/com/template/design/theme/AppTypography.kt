package com.template.design.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Named by role, like [AppColors]. `numeric` exists because tabular figures in a list of
 * measurements need their own style and would otherwise be a one-off `copy()` at each call site.
 */
data class AppTypography(
    val display: TextStyle,
    val title: TextStyle,
    val heading: TextStyle,
    val subtitle: TextStyle,
    val body: TextStyle,
    val bodySmall: TextStyle,
    val label: TextStyle,
    val numeric: TextStyle,
)

/**
 * Uses the platform default family. Point this at a bundled font by declaring it in this module's
 * `composeResources/font/` and swapping `FontFamily.Default` for a `FontFamily(Font(...))`.
 */
fun appTypography(family: FontFamily = FontFamily.Default): AppTypography = AppTypography(
    display = TextStyle(fontFamily = family, fontSize = 34.sp, fontWeight = FontWeight.Bold),
    title = TextStyle(fontFamily = family, fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    heading = TextStyle(fontFamily = family, fontSize = 19.sp, fontWeight = FontWeight.SemiBold),
    subtitle = TextStyle(fontFamily = family, fontSize = 16.sp, fontWeight = FontWeight.Medium),
    body = TextStyle(fontFamily = family, fontSize = 15.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontFamily = family, fontSize = 13.sp, fontWeight = FontWeight.Normal),
    label = TextStyle(fontFamily = family, fontSize = 12.sp, fontWeight = FontWeight.Medium),
    numeric = TextStyle(fontFamily = family, fontSize = 30.sp, fontWeight = FontWeight.Light),
)
