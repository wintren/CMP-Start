package com.template.app.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RoutesTest {

    private val everyDestination = listOf(
        Destination.Locations,
        Destination.BestDay,
        Destination.Settings,
        Destination.Forecast(42L),
    )

    @Test
    fun `every destination survives a round trip`() {
        everyDestination.forEach { destination ->
            assertEquals(destination, destinationOf(destination.toRoute()), destination.toRoute())
        }
    }

    @Test
    fun `a route a link could carry but this app does not know is not a destination`() {
        assertNull(destinationOf(""))
        assertNull(destinationOf("wat"))
        assertNull(destinationOf("forecast"), "no id")
        assertNull(destinationOf("forecast/all"), "an id that is not a number")
    }

    @Test
    fun `a leading slash is what a URL path arrives with`() {
        assertEquals(Destination.Forecast(7L), destinationOf("/forecast/7"))
    }

    @Test
    fun `a detail link gets a list to go back to`() {
        assertEquals(
            listOf(Destination.Locations, Destination.Forecast(3L)),
            stackFor(Destination.Forecast(3L)),
        )
        assertEquals(listOf(Destination.Settings), stackFor(Destination.Settings))
    }

    @Test
    fun `a whole stack survives a round trip`() {
        val stack = listOf(Destination.BestDay, Destination.Forecast(9L))

        assertEquals(stack, backStackOf(stack.toRoutePath()))
    }

    @Test
    fun `a stack keeps whatever still resolves`() {
        assertEquals(listOf(Destination.Locations), backStackOf("locations>gone/13"))
        assertEquals(emptyList(), backStackOf("nonsense"))
    }
}
