package com.template.design.theme

import androidx.compose.ui.graphics.Color

/** Every entry says what it is *for*, never what it looks like, so re-theming is one file. */
data class AppColors(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val primary: Color,
    val onPrimary: Color,
    val accent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val outline: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val onError: Color,
)

private val Ink = Color(0xFF10151C)
private val Slate = Color(0xFF1B222C)
private val Steel = Color(0xFF27313E)
private val Mist = Color(0xFFF4F6F9)
private val Cloud = Color(0xFFFFFFFF)
private val Fog = Color(0xFFE4E9F0)
private val Sky = Color(0xFF2E86DE)
private val SkyBright = Color(0xFF5FA8F5)
private val Amber = Color(0xFFF0A21B)
private val Moss = Color(0xFF2FA36B)
private val Rust = Color(0xFFD3453F)

fun lightAppColors(): AppColors = AppColors(
    isDark = false,
    background = Mist,
    surface = Cloud,
    surfaceRaised = Cloud,
    primary = Sky,
    onPrimary = Color.White,
    accent = Amber,
    textPrimary = Ink,
    textSecondary = Color(0xFF5B6675),
    textDisabled = Color(0xFF9AA5B4),
    outline = Fog,
    success = Moss,
    warning = Amber,
    error = Rust,
    onError = Color.White,
)

fun darkAppColors(): AppColors = AppColors(
    isDark = true,
    background = Ink,
    surface = Slate,
    surfaceRaised = Steel,
    primary = SkyBright,
    onPrimary = Ink,
    accent = Amber,
    textPrimary = Color(0xFFEAF0F7),
    textSecondary = Color(0xFF9AA8B9),
    textDisabled = Color(0xFF5B6675),
    outline = Color(0xFF33404F),
    success = Moss,
    warning = Amber,
    error = Color(0xFFEB6963),
    onError = Ink,
)
