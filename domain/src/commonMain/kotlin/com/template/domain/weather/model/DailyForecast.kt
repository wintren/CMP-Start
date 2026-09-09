package com.template.domain.weather.model

import kotlinx.datetime.LocalDate

/**
 * One forecast day, in canonical units: celsius, metres per second, millimetres.
 *
 * The domain has exactly one unit system. Converting to whatever the user picked is a presentation
 * concern — the moment domain models carry a unit preference, every rule has to ask what it is in.
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
