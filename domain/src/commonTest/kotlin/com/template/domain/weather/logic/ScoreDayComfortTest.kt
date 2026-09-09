package com.template.domain.weather.logic

import com.template.domain.weather.model.ActivityProfile
import com.template.domain.weather.model.ComfortPenalty
import com.template.domain.weather.model.ComfortScore
import com.template.domain.weather.model.DailyForecast
import com.template.domain.weather.model.WeatherCondition
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pure logic needs no fakes, no dispatcher and no test rule — which is the argument for keeping
 * decisions in the `logic` tier. Note that this file names no HTTP, no storage and no Compose.
 */
class ScoreDayComfortTest {

    private val scoreDayComfort = ScoreDayComfort()
    private val running = ActivityProfile.Running.comfort

    @Test
    fun `a day at the ideal temperature with no wind or rain scores full marks`() {
        val score = scoreDayComfort(day(maxTemperatureC = running.idealTemperatureC), running)

        assertEquals(ComfortScore.MAX, score.value)
        assertTrue(score.penalties.isEmpty())
    }

    @Test
    fun `deviation inside the tolerance band is not penalised`() {
        val day = day(maxTemperatureC = running.idealTemperatureC + running.temperatureToleranceC)

        assertEquals(ComfortScore.MAX, scoreDayComfort(day, running).value)
    }

    @Test
    fun `a cold day is penalised and says why`() {
        val score = scoreDayComfort(day(maxTemperatureC = -5.0), running)

        assertTrue(score.value < ComfortScore.MAX)
        assertEquals(listOf(ComfortPenalty.TooCold), score.penalties)
    }

    @Test
    fun `each factor is capped so one bad number cannot zero an otherwise fine day`() {
        val score = scoreDayComfort(day(maxWindSpeedMs = 500.0), running)

        assertEquals(listOf(ComfortPenalty.Windy), score.penalties)
        assertTrue(score.value > 0, "wind alone should not reach zero, was ${score.value}")
    }

    @Test
    fun `a thunderstorm is called out separately from the rain it comes with`() {
        val score = scoreDayComfort(
            day(
                maxTemperatureC = running.idealTemperatureC,
                precipitationMm = 12.0,
                precipitationChancePercent = 95,
                condition = WeatherCondition.Thunderstorm,
            ),
            running,
        )

        assertTrue(ComfortPenalty.StormRisk in score.penalties)
        assertTrue(ComfortPenalty.Wet in score.penalties)
    }

    @Test
    fun `the score never leaves 0 to 100`() {
        val score = scoreDayComfort(
            day(
                maxTemperatureC = -60.0,
                precipitationMm = 300.0,
                precipitationChancePercent = 100,
                maxWindSpeedMs = 90.0,
                condition = WeatherCondition.Thunderstorm,
            ),
            running,
        )

        assertTrue(score.value in 0..ComfortScore.MAX, "was ${score.value}")
    }

    private fun day(
        maxTemperatureC: Double = 14.0,
        precipitationMm: Double = 0.0,
        precipitationChancePercent: Int = 0,
        maxWindSpeedMs: Double = 0.0,
        condition: WeatherCondition = WeatherCondition.Clear,
    ) = DailyForecast(
        date = LocalDate(2026, 6, 1),
        minTemperatureC = maxTemperatureC - 6.0,
        maxTemperatureC = maxTemperatureC,
        precipitationMm = precipitationMm,
        precipitationChancePercent = precipitationChancePercent,
        maxWindSpeedMs = maxWindSpeedMs,
        condition = condition,
    )
}
