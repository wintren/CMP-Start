package com.template.app.weather.forecast

import com.template.app.navigation.NavControls
import com.template.app.weather.forecast.ForecastModels.Action
import com.template.app.weather.forecast.ForecastModels.CurrentBlock
import com.template.app.weather.forecast.ForecastModels.DayItem
import com.template.app.weather.forecast.ForecastModels.State
import com.template.app.weather.format.asDayLabel
import com.template.app.weather.format.asPrecipitation
import com.template.app.weather.format.asTemperature
import com.template.app.weather.format.asTemperatureValue
import com.template.app.weather.format.asWindSpeed
import com.template.app.weather.format.label
import com.template.core.common.flow.combines
import com.template.core.common.logging.Log
import com.template.core.common.time.today
import com.template.core.ui.resource.StringValue
import com.template.core.ui.viewmodel.StateViewModel
import com.template.core.ui.viewmodel.WithActions
import com.template.core.ui.viewmodel.fire
import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.model.DailyForecast
import com.template.domain.weather.model.Forecast
import com.template.domain.weather.model.GeoLocation
import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlin.time.Clock

class ForecastViewModel(
    private val locationId: Long,
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val preferencesRepository: PreferencesRepository,
    private val navControls: NavControls,
    private val clock: Clock,
) : StateViewModel<State>(), WithActions<Action> {

    private data class Local(val error: StringValue? = null)

    private val local = MutableStateFlow(Local())

    override fun initialState() = State()

    override val stateFlow = viewModelState(
        data = {
            combines(
                local,
                locationRepository.observeSaved().map { saved -> saved.firstOrNull { it.id == locationId } },
                forecastRepository.observe(locationId),
                preferencesRepository.observeUnitSystem(),
            )
        },
        state = { (local, location, forecast, units) ->
            // Read once per emission and pass it down, so the formatting functions stay pure.
            val today = clock.today()
            State(
                title = location?.name?.let(StringValue::Raw) ?: StringValue.Empty,
                region = location.regionLabel(),
                isLoading = forecast == null,
                current = forecast?.toCurrentBlock(units),
                days = forecast?.days?.map { it.toDayItem(today, units) }.orEmpty(),
                error = local.error,
            )
        },
        onStarted = { refresh() },
    )

    override fun onAction(action: Action) {
        when (action) {
            Action.OnBack -> navControls.pop()
            Action.OnRefresh -> refresh()
        }
    }

    private fun refresh() = fire(
        onError = { message -> local.update { it.copy(error = message) } },
    ) {
        val location = locationRepository.getSaved().firstOrNull { it.id == locationId }
        if (location == null) {
            Log.w(TAG) { "Forecast opened for unsaved location $locationId" }
            return@fire
        }
        forecastRepository.refresh(location)
    }

    private fun GeoLocation?.regionLabel(): StringValue = when (this) {
        null -> StringValue.Empty
        else -> StringValue.Raw(listOfNotNull(region, country).joinToString(", "))
    }

    private fun Forecast.toCurrentBlock(units: UnitSystem) = CurrentBlock(
        temperature = current.temperatureC.asTemperature(units),
        conditionLabel = current.condition.label(),
        wind = current.windSpeedMs.asWindSpeed(units),
        condition = current.condition,
    )

    private fun DailyForecast.toDayItem(today: LocalDate, units: UnitSystem) = DayItem(
        id = date.toEpochDays().toInt(),
        dayLabel = date.asDayLabel(today),
        condition = condition,
        high = maxTemperatureC.asTemperatureValue(units),
        low = minTemperatureC.asTemperatureValue(units),
        precipitation = precipitationMm.asPrecipitation(units),
        precipitationChance = StringValue.Raw("$precipitationChancePercent%"),
        wind = maxWindSpeedMs.asWindSpeed(units),
    )

    private companion object {
        const val TAG = "ForecastViewModel"
    }
}
