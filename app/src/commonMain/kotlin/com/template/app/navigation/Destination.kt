package com.template.app.navigation

sealed interface Destination {

    data object Locations : Destination

    data class Forecast(val locationId: Long) : Destination

    data object BestDay : Destination

    data object Settings : Destination

    companion object {
        val initial: List<Destination> = listOf(Locations)
    }
}
