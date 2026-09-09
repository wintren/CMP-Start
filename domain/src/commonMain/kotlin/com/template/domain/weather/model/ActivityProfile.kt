package com.template.domain.weather.model

/**
 * The activities the app can rank days for. Deliberately a domain enum rather than a UI list:
 * the numbers are a business rule, and a screen that invented its own would silently disagree
 * with the ranking.
 */
enum class ActivityProfile(val comfort: ComfortProfile) {
    Running(
        ComfortProfile(
            idealTemperatureC = 14.0,
            temperatureToleranceC = 5.0,
            acceptableWindMs = 7.0,
        )
    ),
    Cycling(
        ComfortProfile(
            idealTemperatureC = 19.0,
            temperatureToleranceC = 6.0,
            acceptableWindMs = 5.0,
        )
    ),
    Picnic(
        ComfortProfile(
            idealTemperatureC = 24.0,
            temperatureToleranceC = 4.0,
            acceptableWindMs = 4.0,
        )
    );

    companion object {
        val default: ActivityProfile = Running
    }
}
