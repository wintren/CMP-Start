package com.template.app.fake

import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferencesRepository(
    unitSystem: UnitSystem = UnitSystem.Metric,
    themeMode: ThemeMode = ThemeMode.System,
) : PreferencesRepository {

    val units = MutableStateFlow(unitSystem)
    val theme = MutableStateFlow(themeMode)

    override fun observeUnitSystem(): Flow<UnitSystem> = units

    override fun observeThemeMode(): Flow<ThemeMode> = theme

    override suspend fun setUnitSystem(unitSystem: UnitSystem) {
        units.value = unitSystem
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        theme.value = themeMode
    }
}
