package com.template.domain.weather.model

data class RankedLocationDay(
    val location: GeoLocation,
    val day: DailyForecast,
    val score: ComfortScore,
)
