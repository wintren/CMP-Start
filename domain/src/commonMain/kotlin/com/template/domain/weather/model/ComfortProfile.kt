package com.template.domain.weather.model

/** The knobs scoring needs, as data, so it has no per-activity branches. */
data class ComfortProfile(
    val idealTemperatureC: Double,
    val temperatureToleranceC: Double,
    val acceptableWindMs: Double,
)
