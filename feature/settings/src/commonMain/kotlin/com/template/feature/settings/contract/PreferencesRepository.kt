package com.template.feature.settings.contract

import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.flow.Flow

/**
 * This feature's public surface. Other features read the preference through this interface; nothing
 * outside `:feature:settings` knows how or where it is stored.
 *
 * That one exported contract is the whole cost of promoting a feature to a module — and the reason
 * to keep the export list this short.
 */
interface PreferencesRepository {
    fun observeUnitSystem(): Flow<UnitSystem>
    fun observeThemeMode(): Flow<ThemeMode>

    suspend fun setUnitSystem(unitSystem: UnitSystem)
    suspend fun setThemeMode(themeMode: ThemeMode)
}
