package com.template.feature.settings.contract

import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.flow.Flow

/** This feature's whole public surface. Keeping the export list this short is the point. */
interface PreferencesRepository {
    fun observeUnitSystem(): Flow<UnitSystem>
    fun observeThemeMode(): Flow<ThemeMode>

    suspend fun setUnitSystem(unitSystem: UnitSystem)
    suspend fun setThemeMode(themeMode: ThemeMode)
}
