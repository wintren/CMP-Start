package com.template.feature.settings

import com.template.core.ui.resource.StringValue
import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem

/**
 * The screen's whole contract, in one file: what it shows and what the user can do.
 *
 * One `State` and one sealed `Action` is the entire interface between ViewModel and Composable.
 * The screen receives `state` and `onAction` and nothing else — no ViewModel reference, no
 * callbacks-per-button — which is what makes it previewable and the ViewModel testable.
 *
 * Grouping `State`/`Action`/`Event` in one `object` is the deliberate exception to
 * one-type-per-file: they change together, always.
 */
object SettingsModels {

    data class State(
        val unitSystem: UnitSystem = UnitSystem.default,
        val themeMode: ThemeMode = ThemeMode.default,
        val temperatureExample: StringValue = StringValue.Empty,
    )

    sealed interface Action {
        data object OnBack : Action
        data class OnUnitSystemChange(val unitSystem: UnitSystem) : Action
        data class OnThemeModeChange(val themeMode: ThemeMode) : Action
    }
}
