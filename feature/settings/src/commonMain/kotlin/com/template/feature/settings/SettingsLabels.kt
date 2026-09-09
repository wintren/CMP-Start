package com.template.feature.settings

import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem
import com.template.feature.settings.resources.Res
import com.template.feature.settings.resources.settings_theme_dark
import com.template.feature.settings.resources.settings_theme_light
import com.template.feature.settings.resources.settings_theme_system
import com.template.feature.settings.resources.settings_units_imperial
import com.template.feature.settings.resources.settings_units_metric

/**
 * An enum name is an identifier. These map each one to a word a translator can change, so neither
 * the enum nor the composable holds English.
 */
internal fun UnitSystem.label(): StringValue = when (this) {
    UnitSystem.Metric -> Res.string.settings_units_metric
    UnitSystem.Imperial -> Res.string.settings_units_imperial
}.asValue()

internal fun ThemeMode.label(): StringValue = when (this) {
    ThemeMode.System -> Res.string.settings_theme_system
    ThemeMode.Light -> Res.string.settings_theme_light
    ThemeMode.Dark -> Res.string.settings_theme_dark
}.asValue()
