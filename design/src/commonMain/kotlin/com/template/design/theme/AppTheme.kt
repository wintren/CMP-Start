package com.template.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalAppColors = staticCompositionLocalOf<AppColors> { error("No AppTheme provided") }
private val LocalAppTypography = staticCompositionLocalOf<AppTypography> { error("No AppTheme provided") }
private val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }
private val LocalAppShapes = staticCompositionLocalOf { AppShapes() }
private val LocalAppSizing = staticCompositionLocalOf { AppSizing() }

/**
 * Wraps [MaterialTheme] rather than replacing it, and feeds it a scheme derived from
 * [AppColors] so a stray M3 component never renders off-palette.
 */
@Composable
fun AppTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = remember(isDark) { if (isDark) darkAppColors() else lightAppColors() }
    val typography = remember { appTypography() }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppSpacing provides AppSpacing(),
        LocalAppShapes provides AppShapes(),
        LocalAppSizing provides AppSizing(),
    ) {
        MaterialTheme(
            colorScheme = remember(colors) { colors.toMaterialScheme() },
            content = content,
        )
    }
}

object AppTheme {
    val colors: AppColors
        @Composable @ReadOnlyComposable get() = LocalAppColors.current

    val typography: AppTypography
        @Composable @ReadOnlyComposable get() = LocalAppTypography.current

    val spacing: AppSpacing
        @Composable @ReadOnlyComposable get() = LocalAppSpacing.current

    val shapes: AppShapes
        @Composable @ReadOnlyComposable get() = LocalAppShapes.current

    val sizing: AppSizing
        @Composable @ReadOnlyComposable get() = LocalAppSizing.current
}

private fun AppColors.toMaterialScheme() = when (isDark) {
    true -> darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        secondary = accent,
        background = background,
        onBackground = textPrimary,
        surface = surface,
        onSurface = textPrimary,
        surfaceVariant = surfaceRaised,
        onSurfaceVariant = textSecondary,
        outline = outline,
        error = error,
        onError = onError,
    )
    false -> lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        secondary = accent,
        background = background,
        onBackground = textPrimary,
        surface = surface,
        onSurface = textPrimary,
        surfaceVariant = surfaceRaised,
        onSurfaceVariant = textSecondary,
        outline = outline,
        error = error,
        onError = onError,
    )
}
