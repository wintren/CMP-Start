package com.template.app.navigation.entries

import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import com.template.app.navigation.Destination
import com.template.app.weather.bestday.BestDayScreen
import com.template.app.weather.bestday.BestDayViewModel
import com.template.app.weather.forecast.ForecastScreen
import com.template.app.weather.forecast.ForecastViewModel
import com.template.app.weather.locations.LocationsScreen
import com.template.app.weather.locations.LocationsViewModel
import com.template.core.ui.viewmodel.collectState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<Destination>.weatherEntries() {

    entry<Destination.Locations> {
        val viewModel: LocationsViewModel = koinViewModel()
        val state by viewModel.collectState()
        LocationsScreen(state = state, onAction = viewModel::onAction)
    }

    entry<Destination.Forecast> { destination ->
        // Keyed by id: two forecast screens on the stack must not share one ViewModel.
        val viewModel: ForecastViewModel = koinViewModel(
            key = "forecast-${destination.locationId}",
        ) { parametersOf(destination.locationId) }
        val state by viewModel.collectState()
        ForecastScreen(state = state, onAction = viewModel::onAction)
    }

    entry<Destination.BestDay> {
        val viewModel: BestDayViewModel = koinViewModel()
        val state by viewModel.collectState()
        BestDayScreen(state = state, onAction = viewModel::onAction)
    }
}
