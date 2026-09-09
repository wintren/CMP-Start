package com.template.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.template.app.resources.Res
import com.template.app.resources.tab_best_day
import com.template.app.resources.tab_places
import com.template.app.resources.tab_settings
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue

/** The top-level destinations reachable from the bottom bar. */
enum class NavTab(
    val destination: Destination,
    val icon: ImageVector,
    val label: StringValue,
) {
    Locations(Destination.Locations, Icons.Default.Place, Res.string.tab_places.asValue()),
    BestDay(Destination.BestDay, Icons.Default.Star, Res.string.tab_best_day.asValue()),
    Settings(Destination.Settings, Icons.Default.Settings, Res.string.tab_settings.asValue());

    companion object {
        fun of(destination: Destination): NavTab? =
            entries.firstOrNull { it.destination == destination }
    }
}
