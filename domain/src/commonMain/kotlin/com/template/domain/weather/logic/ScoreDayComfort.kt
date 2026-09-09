package com.template.domain.weather.logic

import com.template.domain.weather.model.ComfortPenalty
import com.template.domain.weather.model.ComfortProfile
import com.template.domain.weather.model.ComfortScore
import com.template.domain.weather.model.DailyForecast
import com.template.domain.weather.model.WeatherCondition
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Pure: scores one day against one activity profile.
 *
 * "Pure" is the whole point of the `logic` tier — same inputs, same answer, no clock, no I/O, no
 * randomness. That is what makes the rule testable without a single fake, and what lets you argue
 * about the numbers without booting the app.
 *
 * Deductions are capped per factor so no single bad number can zero out an otherwise fine day,
 * and each deduction that actually bites records why.
 */
class ScoreDayComfort {

    operator fun invoke(day: DailyForecast, profile: ComfortProfile): ComfortScore {
        val penalties = mutableListOf<ComfortPenalty>()
        var deduction = 0.0

        val temperatureMiss = abs(day.maxTemperatureC - profile.idealTemperatureC) -
            profile.temperatureToleranceC
        if (temperatureMiss > 0) {
            deduction += (temperatureMiss * TEMPERATURE_WEIGHT).coerceAtMost(TEMPERATURE_CAP)
            penalties += when {
                day.maxTemperatureC < profile.idealTemperatureC -> ComfortPenalty.TooCold
                else -> ComfortPenalty.TooWarm
            }
        }

        val windExcess = day.maxWindSpeedMs - profile.acceptableWindMs
        if (windExcess > 0) {
            deduction += (windExcess * WIND_WEIGHT).coerceAtMost(WIND_CAP)
            penalties += ComfortPenalty.Windy
        }

        val wetness = day.precipitationMm * PRECIPITATION_MM_WEIGHT +
            (day.precipitationChancePercent - DRY_CHANCE_PERCENT).coerceAtLeast(0) *
            PRECIPITATION_CHANCE_WEIGHT
        if (wetness > 0) {
            deduction += wetness.coerceAtMost(PRECIPITATION_CAP)
            penalties += ComfortPenalty.Wet
        }

        when (day.condition) {
            WeatherCondition.Thunderstorm -> {
                deduction += STORM_DEDUCTION
                penalties += ComfortPenalty.StormRisk
            }
            WeatherCondition.Snow -> deduction += SNOW_DEDUCTION
            WeatherCondition.Fog -> {
                deduction += FOG_DEDUCTION
                penalties += ComfortPenalty.PoorVisibility
            }
            else -> Unit
        }

        val value = (ComfortScore.MAX - deduction).roundToInt().coerceIn(0, ComfortScore.MAX)
        return ComfortScore(value = value, penalties = penalties.distinct())
    }

    private companion object {
        const val TEMPERATURE_WEIGHT = 3.0
        const val TEMPERATURE_CAP = 40.0
        const val WIND_WEIGHT = 5.0
        const val WIND_CAP = 25.0
        const val PRECIPITATION_MM_WEIGHT = 4.0
        const val PRECIPITATION_CHANCE_WEIGHT = 0.3
        const val PRECIPITATION_CAP = 30.0
        const val DRY_CHANCE_PERCENT = 20
        const val STORM_DEDUCTION = 25.0
        const val SNOW_DEDUCTION = 15.0
        const val FOG_DEDUCTION = 8.0
    }
}
