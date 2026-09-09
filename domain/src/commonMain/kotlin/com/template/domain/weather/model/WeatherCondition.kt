package com.template.domain.weather.model

/** The provider's WMO codes collapsed; the domain never sees a raw code. */
enum class WeatherCondition {
    Clear,
    PartlyCloudy,
    Overcast,
    Fog,
    Drizzle,
    Rain,
    Snow,
    Thunderstorm,
    Unknown,
}
