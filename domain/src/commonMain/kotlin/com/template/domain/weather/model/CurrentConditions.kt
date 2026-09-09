package com.template.domain.weather.model

data class CurrentConditions(
    val temperatureC: Double,
    val windSpeedMs: Double,
    val condition: WeatherCondition,
)
