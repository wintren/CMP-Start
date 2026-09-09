package com.template.feature.settings.model

enum class ThemeMode {
    System,
    Light,
    Dark;

    companion object {
        val default: ThemeMode = System

        fun fromName(name: String?): ThemeMode =
            entries.firstOrNull { it.name == name } ?: default
    }
}
