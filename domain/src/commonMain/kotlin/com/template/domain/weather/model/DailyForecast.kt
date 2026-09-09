package com.template.domain.weather.model

import kotlinx.datetime.LocalDate

/**
 * Canonical units: celsius, metres per second, millimetres. The domain has exactly one unit
 * system — the moment a model carries a preference, every rule has to ask what it is in.
 */
data class DailyForecast(
    val date: LocalDate,
    val minTemperatureC: Double,
    val maxTemperatureC: Double,
    val precipitationMm: Double,
    val precipitationChancePercent: Int,
    val maxWindSpeedMs: Double,
    val condition: WeatherCondition,
)
