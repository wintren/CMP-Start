package com.template.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/** The top-level destinations reachable from the bottom bar. */
enum class NavTab(
    val destination: Destination,
    val icon: ImageVector,
    val label: String,
) {
    Locations(Destination.Locations, Icons.Default.Place, "Places"),
    BestDay(Destination.BestDay, Icons.Default.Star, "Best day"),
    Settings(Destination.Settings, Icons.Default.Settings, "Settings");

    companion object {
        fun of(destination: Destination): NavTab? =
            entries.firstOrNull { it.destination == destination }
    }
}
