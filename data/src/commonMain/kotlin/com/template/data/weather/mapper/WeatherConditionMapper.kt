package com.template.data.weather.mapper

import com.template.domain.weather.model.WeatherCondition

/**
 * WMO 4677 codes, as Open-Meteo reports them, collapsed into the conditions the app distinguishes.
 * The full table lives at https://open-meteo.com/en/docs — the grouping below is ours.
 */
internal fun Int?.toWeatherCondition(): WeatherCondition = when (this) {
    null -> WeatherCondition.Unknown
    0 -> WeatherCondition.Clear
    1, 2 -> WeatherCondition.PartlyCloudy
    3 -> WeatherCondition.Overcast
    45, 48 -> WeatherCondition.Fog
    51, 53, 55, 56, 57 -> WeatherCondition.Drizzle
    61, 63, 65, 66, 67, 80, 81, 82 -> WeatherCondition.Rain
    71, 73, 75, 77, 85, 86 -> WeatherCondition.Snow
    95, 96, 99 -> WeatherCondition.Thunderstorm
    else -> WeatherCondition.Unknown
}
