package com.template.app.di

import com.template.app.AppViewModel
import com.template.app.navigation.BackStackStore
import com.template.app.navigation.NavControls
import com.template.app.navigation.Navigator
import com.template.app.weather.bestday.BestDayViewModel
import com.template.app.weather.forecast.ForecastViewModel
import com.template.app.weather.locations.LocationsViewModel
import com.template.di.clientModules
import com.template.feature.settings.settingsFeatureModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val uiModule = module {
    single { Navigator() }
    // Not `new(::…)`: that would demand a `Duration` from the graph rather than take the default.
    single { BackStackStore(store = get(), clock = get()) }
    // The same instance, seen as the narrow interface. Not a second Navigator.
    single<NavControls> { get<Navigator>() }

    viewModelOf(::AppViewModel)
    viewModelOf(::LocationsViewModel)
    viewModelOf(::BestDayViewModel)
    // Takes a runtime argument, so it cannot use `viewModelOf`.
    viewModel { parameters ->
        ForecastViewModel(parameters.get(), get(), get(), get(), get(), get())
    }
}

val appModules: List<Module> = buildList {
    addAll(clientModules)
    add(uiModule)
    add(settingsFeatureModule)
}
