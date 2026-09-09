package com.template.data.weather.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Open-Meteo returns daily values column-oriented — parallel arrays indexed by day. */
@Serializable
internal data class ForecastResponse(
    val current: CurrentDto? = null,
    val daily: DailyDto? = null,
)

@Serializable
internal data class CurrentDto(
    @SerialName("temperature_2m") val temperatureC: Double? = null,
    @SerialName("wind_speed_10m") val windSpeedMs: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
)

@Serializable
internal data class DailyDto(
    @SerialName("time") val dates: List<String> = emptyList(),
    @SerialName("weather_code") val weatherCodes: List<Int?> = emptyList(),
    @SerialName("temperature_2m_max") val maxTemperatures: List<Double?> = emptyList(),
    @SerialName("temperature_2m_min") val minTemperatures: List<Double?> = emptyList(),
    @SerialName("precipitation_sum") val precipitationSums: List<Double?> = emptyList(),
    @SerialName("precipitation_probability_max") val precipitationChances: List<Int?> = emptyList(),
    @SerialName("wind_speed_10m_max") val maxWindSpeeds: List<Double?> = emptyList(),
)
