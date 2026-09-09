package com.template.domain.weather.model

data class RankedDay(
    val day: DailyForecast,
    val score: ComfortScore,
)
