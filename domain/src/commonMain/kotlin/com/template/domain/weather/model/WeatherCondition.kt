package com.template.domain.weather.model

/**
 * The provider's WMO weather codes collapsed into the conditions this app actually distinguishes.
 * Mapping the wire's 28 codes happens in `:data`; the domain never sees a raw code.
 */
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
