package com.template.domain.weather.model

/**
 * What "good weather" means for one activity. The knobs a rule needs, stated as data, so scoring
 * has no per-activity branches in it.
 */
data class ComfortProfile(
    val idealTemperatureC: Double,
    val temperatureToleranceC: Double,
    val acceptableWindMs: Double,
)
