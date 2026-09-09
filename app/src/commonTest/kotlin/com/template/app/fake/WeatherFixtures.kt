package com.template.app.fake

import com.template.domain.weather.model.CurrentConditions
import com.template.domain.weather.model.DailyForecast
import com.template.domain.weather.model.Forecast
import com.template.domain.weather.model.GeoLocation
import com.template.domain.weather.model.WeatherCondition
import kotlinx.datetime.LocalDate

fun geoLocation(
    id: Long = 1L,
    name: String = "Gothenburg",
) = GeoLocation(
    id = id,
    name = name,
    country = "Sweden",
    region = "Västra Götaland County",
    latitude = 57.70716,
    longitude = 11.96679,
    timeZone = "Europe/Stockholm",
)

fun dailyForecast(
    date: LocalDate = LocalDate(2026, 6, 1),
    maxTemperatureC: Double = 18.0,
    precipitationMm: Double = 0.0,
    precipitationChancePercent: Int = 0,
    maxWindSpeedMs: Double = 2.0,
    condition: WeatherCondition = WeatherCondition.Clear,
) = DailyForecast(
    date = date,
    minTemperatureC = maxTemperatureC - 6.0,
    maxTemperatureC = maxTemperatureC,
    precipitationMm = precipitationMm,
    precipitationChancePercent = precipitationChancePercent,
    maxWindSpeedMs = maxWindSpeedMs,
    condition = condition,
)

fun forecast(
    locationId: Long = 1L,
    days: List<DailyForecast> = listOf(dailyForecast()),
) = Forecast(
    locationId = locationId,
    current = CurrentConditions(
        temperatureC = 17.0,
        windSpeedMs = 3.0,
        condition = WeatherCondition.Clear,
    ),
    days = days,
)
