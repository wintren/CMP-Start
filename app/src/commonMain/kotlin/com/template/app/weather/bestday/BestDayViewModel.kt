package com.template.app.weather.bestday

import com.template.app.navigation.Destination
import com.template.app.navigation.NavControls
import com.template.app.weather.bestday.BestDayModels.Action
import com.template.app.weather.bestday.BestDayModels.RankedItem
import com.template.app.weather.bestday.BestDayModels.State
import com.template.app.weather.format.asDayLabel
import com.template.app.weather.format.asTemperatureValue
import com.template.core.common.flow.combines
import com.template.core.common.logging.Log
import com.template.core.ui.resource.StringValue
import com.template.core.ui.viewmodel.StateViewModel
import com.template.core.ui.viewmodel.WithActions
import com.template.core.ui.viewmodel.fire
import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.logic.RankSavedLocationDays
import com.template.domain.weather.model.ActivityProfile
import com.template.domain.weather.model.RankedLocationDay
import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

/**
 * The screen that earns the `parameters` overload of `viewModelState`.
 *
 * `rankSavedLocationDays(activity)` is a *different flow* per activity. With everything in one
 * `combines`, changing the activity would have to rebuild the whole graph. Putting the activity in
 * `parameters` re-subscribes `data` through `flatMapLatest` instead: pick Cycling and the previous
 * ranking is cancelled and replaced with exactly one emission.
 *
 * Ranking itself is not here. [RankSavedLocationDays] joins two repositories and sequences pure
 * scoring logic — a ViewModel doing that would put a business rule somewhere no test will look.
 */
class BestDayViewModel(
    private val rankSavedLocationDays: RankSavedLocationDays,
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val preferencesRepository: PreferencesRepository,
    private val navControls: NavControls,
) : StateViewModel<State>(), WithActions<Action> {

    private data class Local(
        val activity: ActivityProfile = ActivityProfile.default,
        val isRefreshing: Boolean = false,
        val error: StringValue? = null,
    )

    private val local = MutableStateFlow(Local())

    override fun initialState() = State()

    override val stateFlow = viewModelState(
        parameters = { local },
        data = { current ->
            combines(
                flowOf(current),
                rankSavedLocationDays(current.activity),
                preferencesRepository.observeUnitSystem(),
            )
        },
        state = { (current, ranked, units) ->
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            State(
                activity = current.activity,
                ranked = ranked.map { it.toRankedItem(today, units) },
                isRefreshing = current.isRefreshing,
                error = current.error,
            )
        },
        onStarted = { refresh() },
    )

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnActivityChange -> local.update { it.copy(activity = action.activity) }
            is Action.OnOpenPlace -> navControls.navigateTo(Destination.Forecast(action.locationId))
            Action.OnRefresh -> refresh()
        }
    }

    private fun refresh() = fire {
        local.update { it.copy(isRefreshing = true, error = null) }
        runCatching { forecastRepository.refreshAll(locationRepository.getSaved()) }
            .onFailure { error ->
                Log.w(TAG) { "Refresh failed: ${error.message}" }
                local.update { it.copy(error = StringValue.Raw(REFRESH_FAILED)) }
            }
        local.update { it.copy(isRefreshing = false) }
    }

    private fun RankedLocationDay.toRankedItem(today: LocalDate, units: UnitSystem) = RankedItem(
        key = "${location.id}-${day.date}",
        locationId = location.id,
        place = StringValue.Raw(location.name),
        dayLabel = day.date.asDayLabel(today),
        temperature = day.maxTemperatureC.asTemperatureValue(units),
        condition = day.condition,
        score = score,
        penalties = score.penalties,
    )

    private companion object {
        const val TAG = "BestDayViewModel"
        const val REFRESH_FAILED = "Could not refresh the forecasts."
    }
}
