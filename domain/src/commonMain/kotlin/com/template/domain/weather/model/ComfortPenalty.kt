package com.template.domain.weather.model

/** Why a day scored below perfect. Carried in [ComfortScore] so the UI explains, not just ranks. */
enum class ComfortPenalty {
    TooCold,
    TooWarm,
    Windy,
    Wet,
    PoorVisibility,
    StormRisk,
}
