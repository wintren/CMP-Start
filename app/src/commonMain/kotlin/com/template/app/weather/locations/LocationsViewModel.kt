package com.template.app.weather.locations

import com.template.app.navigation.Destination
import com.template.app.navigation.NavControls
import com.template.app.weather.format.asTemperature
import com.template.app.weather.format.label
import com.template.app.weather.locations.LocationsModels.Action
import com.template.app.weather.locations.LocationsModels.PlaceItem
import com.template.app.weather.locations.LocationsModels.SavedItem
import com.template.app.weather.locations.LocationsModels.State
import com.template.core.common.flow.combines
import com.template.core.common.logging.Log
import com.template.core.ui.resource.StringValue
import com.template.core.ui.viewmodel.StateViewModel
import com.template.core.ui.viewmodel.WithActions
import com.template.core.ui.viewmodel.fire
import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.contract.PlaceSearchRepository
import com.template.domain.weather.model.Forecast
import com.template.domain.weather.model.GeoLocation
import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Note the shape of the local state: **one** `private MutableStateFlow` holding a small [Local]
 * record, not one flow per field.
 *
 * Both this project's reference apps grew ViewModels with eight or nine parallel `MutableStateFlow`
 * properties, and it costs you twice — `combines` runs out of arity, and a single user action
 * that touches three of them emits three times, so the screen recomposes against states that never
 * logically existed. One record updated atomically emits once.
 */
class LocationsViewModel(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val placeSearchRepository: PlaceSearchRepository,
    private val preferencesRepository: PreferencesRepository,
    private val navControls: NavControls,
) : StateViewModel<State>(), WithActions<Action> {

    private data class Local(
        val query: String = "",
        val isSearching: Boolean = false,
        val searchResults: List<GeoLocation> = emptyList(),
        val isRefreshing: Boolean = false,
        val error: StringValue? = null,
    )

    private val local = MutableStateFlow(Local())
    private var searchJob: Job? = null

    override fun initialState() = State()

    override val stateFlow = viewModelState(
        data = {
            combines(
                local,
                locationRepository.observeSaved(),
                forecastRepository.observeAll(),
                preferencesRepository.observeUnitSystem(),
            )
        },
        state = { (local, saved, forecasts, units) ->
            State(
                query = local.query,
                isSearching = local.isSearching,
                searchResults = local.searchResults.map { it.toPlaceItem() },
                saved = saved.map { it.toSavedItem(forecasts[it.id], units) },
                isRefreshing = local.isRefreshing,
                error = local.error,
            )
        },
        onStarted = { refreshAll() },
    )

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnQueryChange -> onQueryChanged(action.query)
            Action.OnSearchSubmit -> search(state.query)
            is Action.OnAddPlace -> addPlace(action.placeId)
            is Action.OnRemoveSaved -> fire { locationRepository.remove(action.locationId) }
            is Action.OnOpenSaved ->
                navControls.navigateTo(Destination.Forecast(action.locationId))
            Action.OnRefresh -> refreshAll()
            Action.OnDismissError -> local.update { it.copy(error = null) }
        }
    }

    private fun onQueryChanged(query: String) {
        local.update { it.copy(query = query, error = null) }
        // Restart the debounce on every keystroke: cancelling the previous job is what keeps this
        // from firing a request per character.
        searchJob?.cancel()
        if (query.isBlank()) {
            local.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        searchJob = fire {
            delay(SEARCH_DEBOUNCE_MS)
            search(query)
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = fire {
            local.update { it.copy(isSearching = true) }
            // Errors are thrown by `:data` and caught here — the ViewModel is the first layer that
            // can turn one into something a person can read.
            runCatching { placeSearchRepository.search(query.trim()) }
                .onSuccess { results ->
                    local.update { it.copy(searchResults = results, isSearching = false) }
                }
                .onFailure { error ->
                    Log.w(TAG) { "Place search failed: ${error.message}" }
                    local.update {
                        it.copy(isSearching = false, error = StringValue.Raw(SEARCH_FAILED))
                    }
                }
        }
    }

    private fun addPlace(placeId: Long) {
        val place = local.value.searchResults.firstOrNull { it.id == placeId } ?: return
        fire {
            locationRepository.save(place)
            local.update { it.copy(query = "", searchResults = emptyList()) }
            refresh(place)
        }
    }

    private fun refreshAll() = fire {
        local.update { it.copy(isRefreshing = true) }
        runCatching { forecastRepository.refreshAll(locationRepository.getSaved()) }
            .onFailure { error ->
                Log.w(TAG) { "Refresh failed: ${error.message}" }
                local.update { it.copy(error = StringValue.Raw(REFRESH_FAILED)) }
            }
        local.update { it.copy(isRefreshing = false) }
    }

    private fun refresh(location: GeoLocation) = fire {
        runCatching { forecastRepository.refresh(location) }
            .onFailure { local.update { state -> state.copy(error = StringValue.Raw(REFRESH_FAILED)) } }
    }

    private fun GeoLocation.toPlaceItem() = PlaceItem(
        id = id,
        name = StringValue.Raw(name),
        region = StringValue.Raw(listOfNotNull(region, country).joinToString(", ")),
    )

    private fun GeoLocation.toSavedItem(forecast: Forecast?, units: UnitSystem) = SavedItem(
        id = id,
        name = StringValue.Raw(name),
        region = StringValue.Raw(listOfNotNull(region, country).joinToString(", ")),
        temperature = forecast?.current?.temperatureC?.asTemperature(units) ?: StringValue.Raw("—"),
        conditionLabel = forecast?.current?.condition?.label() ?: StringValue.Empty,
        condition = forecast?.current?.condition ?: com.template.domain.weather.model.WeatherCondition.Unknown,
    )

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
        const val TAG = "LocationsViewModel"
        const val SEARCH_FAILED = "Could not search right now."
        const val REFRESH_FAILED = "Could not refresh the forecast."
    }
}
