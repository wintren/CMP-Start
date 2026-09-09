package com.template.app.navigation

/**
 * Every place the app can be, as one sealed tree. Type-safe by construction: a destination that
 * needs an id cannot be built without one, so there are no string routes to typo and no argument
 * bundles to unpack.
 */
sealed interface Destination {

    data object Locations : Destination

    data class Forecast(val locationId: Long) : Destination

    data object BestDay : Destination

    data object Settings : Destination

    companion object {
        val initial: List<Destination> = listOf(Locations)
    }
}
