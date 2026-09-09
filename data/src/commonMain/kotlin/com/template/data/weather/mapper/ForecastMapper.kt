package com.template.data.weather.mapper

import com.template.data.weather.source.remote.dto.ForecastResponse
import com.template.domain.weather.model.CurrentConditions
import com.template.domain.weather.model.DailyForecast
import com.template.domain.weather.model.Forecast
import kotlinx.datetime.LocalDate

/**
 * The mapping membrane: column-oriented wire arrays in, a list of days out.
 *
 * A day is emitted only when every value it needs is present. Open-Meteo can return a shorter or
 * null-padded array at the edge of its range, and a `DailyForecast` with a fabricated 0.0 in it
 * would score as a perfect day — silently wrong is worse than absent.
 */
internal fun ForecastResponse.toForecast(locationId: Long): Forecast = Forecast(
    locationId = locationId,
    current = CurrentConditions(
        temperatureC = current?.temperatureC ?: 0.0,
        windSpeedMs = current?.windSpeedMs ?: 0.0,
        condition = current?.weatherCode.toWeatherCondition(),
    ),
    days = toDays(),
)

private fun ForecastResponse.toDays(): List<DailyForecast> {
    val daily = daily ?: return emptyList()
    return daily.dates.indices.mapNotNull { index ->
        val date = runCatching { LocalDate.parse(daily.dates[index]) }.getOrNull()
        val max = daily.maxTemperatures.getOrNull(index)
        val min = daily.minTemperatures.getOrNull(index)
        if (date == null || max == null || min == null) return@mapNotNull null

        DailyForecast(
            date = date,
            minTemperatureC = min,
            maxTemperatureC = max,
            precipitationMm = daily.precipitationSums.getOrNull(index) ?: 0.0,
            precipitationChancePercent = daily.precipitationChances.getOrNull(index) ?: 0,
            maxWindSpeedMs = daily.maxWindSpeeds.getOrNull(index) ?: 0.0,
            condition = daily.weatherCodes.getOrNull(index).toWeatherCondition(),
        )
    }
}
