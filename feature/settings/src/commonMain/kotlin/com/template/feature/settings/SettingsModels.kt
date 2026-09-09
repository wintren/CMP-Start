package com.template.feature.settings

import com.template.core.ui.resource.StringValue
import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem

private fun UnitSystem.asOption() = SettingsModels.Option(this, label())

private fun ThemeMode.asOption() = SettingsModels.Option(this, label())

/** `State` and `Action` share a file: they change together, always. */
object SettingsModels {

    data class State(
        val unitSystem: UnitSystem = UnitSystem.default,
        val themeMode: ThemeMode = ThemeMode.default,
        val unitOptions: List<Option<UnitSystem>> = UnitSystem.entries.map { it.asOption() },
        val themeOptions: List<Option<ThemeMode>> = ThemeMode.entries.map { it.asOption() },
        val temperatureExample: StringValue = StringValue.Empty,
    )

    /** A choice with its word already chosen. The screen renders `label` and hands `value` back. */
    data class Option<T>(val value: T, val label: StringValue)

    sealed interface Action {
        data object OnBack : Action
        data class OnUnitSystemChange(val unitSystem: UnitSystem) : Action
        data class OnThemeModeChange(val themeMode: ThemeMode) : Action
    }
}
