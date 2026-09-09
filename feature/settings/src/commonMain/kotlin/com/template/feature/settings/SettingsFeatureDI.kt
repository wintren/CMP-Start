package com.template.feature.settings

import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.internal.PreferencesRepositoryImpl
import org.koin.core.module.dsl.new
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * `onBack` is a parameter, not injected: this feature must not know the app's navigator, or
 * it stops being liftable into another project.
 */
val settingsFeatureModule = module {
    single<PreferencesRepository> { new(::PreferencesRepositoryImpl) }
    viewModel { parameters -> SettingsViewModel(get(), parameters.get()) }
}
