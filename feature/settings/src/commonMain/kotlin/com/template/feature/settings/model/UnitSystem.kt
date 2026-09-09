package com.template.feature.settings.model

/**
 * The user's unit preference. A *presentation* concern: the domain works in one canonical system
 * (celsius, m/s, mm) and this decides how those numbers are shown.
 */
enum class UnitSystem {
    Metric,
    Imperial;

    companion object {
        val default: UnitSystem = Metric

        fun fromName(name: String?): UnitSystem =
            entries.firstOrNull { it.name == name } ?: default
    }
}
