package com.template.data.weather.source.remote

import com.template.data.weather.source.remote.dto.ForecastResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class OpenMeteoForecastSource(private val client: HttpClient) {

    suspend fun fetch(latitude: Double, longitude: Double): ForecastResponse =
        client.get(FORECAST_URL) {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("current", CURRENT_FIELDS)
            parameter("daily", DAILY_FIELDS)
            parameter("forecast_days", FORECAST_DAYS)
            parameter("timezone", "auto")
            // Requested in the domain's canonical units, so the mapper never converts.
            parameter("temperature_unit", "celsius")
            parameter("wind_speed_unit", "ms")
            parameter("precipitation_unit", "mm")
        }.body()

    private companion object {
        const val FORECAST_URL = "https://api.open-meteo.com/v1/forecast"
        const val CURRENT_FIELDS = "temperature_2m,weather_code,wind_speed_10m"
        const val DAILY_FIELDS =
            "weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum," +
                "precipitation_probability_max,wind_speed_10m_max"
        const val FORECAST_DAYS = 7
    }
}
