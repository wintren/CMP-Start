package com.template.domain.weather.model

data class Forecast(
    val locationId: Long,
    val current: CurrentConditions,
    val days: List<DailyForecast>,
)
