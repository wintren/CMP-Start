package com.template.feature.settings.internal

import com.template.core.common.storage.KeyValueStore
import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * An impl split from its interface inside the same module lives in `internal/` — see
 * docs/architecture.md. Enum names are stored, not ordinals: reordering the enum must not silently
 * change what everyone's saved preference means.
 */
internal class PreferencesRepositoryImpl(
    private val keyValueStore: KeyValueStore,
) : PreferencesRepository {

    override fun observeUnitSystem(): Flow<UnitSystem> =
        keyValueStore.observeString(KEY_UNIT_SYSTEM).map(UnitSystem::fromName)

    override fun observeThemeMode(): Flow<ThemeMode> =
        keyValueStore.observeString(KEY_THEME_MODE).map(ThemeMode::fromName)

    override suspend fun setUnitSystem(unitSystem: UnitSystem) =
        keyValueStore.putString(KEY_UNIT_SYSTEM, unitSystem.name)

    override suspend fun setThemeMode(themeMode: ThemeMode) =
        keyValueStore.putString(KEY_THEME_MODE, themeMode.name)

    private companion object {
        const val KEY_UNIT_SYSTEM = "settings.unit_system"
        const val KEY_THEME_MODE = "settings.theme_mode"
    }
}
