package com.template.feature.settings

import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.internal.PreferencesRepositoryImpl
import org.koin.core.module.dsl.new
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * The feature module carries its own wiring, including its ViewModel.
 *
 * `onBack` is passed in as a parameter rather than injected: this feature must not know about the
 * app's navigator, or it would depend on `:app` and stop being liftable into another project.
 */
val settingsFeatureModule = module {
    single<PreferencesRepository> { new(::PreferencesRepositoryImpl) }
    viewModel { parameters -> SettingsViewModel(get(), parameters.get()) }
}
