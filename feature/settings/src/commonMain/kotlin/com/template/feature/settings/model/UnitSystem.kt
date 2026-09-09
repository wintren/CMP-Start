package com.template.feature.settings.model

/** A presentation concern: the domain works in one canonical system and this picks the display. */
enum class UnitSystem {
    Metric,
    Imperial;

    companion object {
        val default: UnitSystem = Metric

        fun fromName(name: String?): UnitSystem =
            entries.firstOrNull { it.name == name } ?: default
    }
}
