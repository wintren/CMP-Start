package com.template.data.weather.mapper

import com.template.data.weather.source.remote.dto.ForecastResponse
import com.template.domain.weather.model.WeatherCondition
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForecastMapperTest {

    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    private fun map(raw: String) =
        json.decodeFromString<ForecastResponse>(raw).toForecast(locationId = 42L)

    @Test
    fun `column-oriented arrays become one day per index`() {
        val forecast = map(FORECAST_JSON)

        assertEquals(7, forecast.days.size)
        assertEquals(LocalDate(2026, 9, 9), forecast.days.first().date)
        assertEquals(LocalDate(2026, 9, 15), forecast.days.last().date)
    }

    @Test
    fun `values land on the day they belong to`() {
        // Index 5 in every array: the thunderstorm day.
        val stormDay = map(FORECAST_JSON).days[5]

        assertEquals(LocalDate(2026, 9, 14), stormDay.date)
        assertEquals(23.6, stormDay.maxTemperatureC)
        assertEquals(14.2, stormDay.minTemperatureC)
        assertEquals(11.2, stormDay.precipitationMm)
        assertEquals(92, stormDay.precipitationChancePercent)
        assertEquals(9.3, stormDay.maxWindSpeedMs)
        assertEquals(WeatherCondition.Thunderstorm, stormDay.condition)
    }

    @Test
    fun `WMO codes collapse to the conditions the app shows`() {
        val conditions = map(FORECAST_JSON).days.map { it.condition }

        assertEquals(
            listOf(
                WeatherCondition.Overcast,
                WeatherCondition.Rain,
                WeatherCondition.Overcast,
                WeatherCondition.Overcast,
                WeatherCondition.Rain,
                WeatherCondition.Thunderstorm,
                WeatherCondition.Overcast,
            ),
            conditions,
        )
    }

    @Test
    fun `current conditions are mapped`() {
        val current = map(FORECAST_JSON).current

        assertEquals(19.5, current.temperatureC)
        assertEquals(4.0, current.windSpeedMs)
        assertEquals(WeatherCondition.Overcast, current.condition)
    }

    @Test
    fun `a day missing a temperature is dropped rather than defaulted`() {
        // The regression this guards: a fabricated 0.0 would score as a perfect day.
        val days = map(RAGGED_FORECAST_JSON).days

        assertEquals(1, days.size, "only the first day has both temperatures")
        assertEquals(LocalDate(2026, 9, 9), days.single().date)
    }

    @Test
    fun `an absent optional falls back without dropping the day`() {
        val day = map(RAGGED_FORECAST_JSON).days.single()

        assertEquals(0, day.precipitationChancePercent)
        assertEquals(WeatherCondition.Clear, day.condition)
    }

    @Test
    fun `an unknown code is not guessed at`() {
        val forecast = map("""{"daily":{"time":["2026-01-01"],"weather_code":[1234],"temperature_2m_max":[5.0],"temperature_2m_min":[1.0]}}""")

        assertEquals(WeatherCondition.Unknown, forecast.days.single().condition)
    }

    @Test
    fun `an empty response maps to no days rather than throwing`() {
        assertTrue(map("{}").days.isEmpty())
    }
}
