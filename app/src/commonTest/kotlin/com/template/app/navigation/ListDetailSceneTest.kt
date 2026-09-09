package com.template.app.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * The whole adaptive decision is this function; everything else is layout. It runs on a back stack
 * the navigator built without knowing the window size, which is why a resize needs no state.
 */
class ListDetailSceneTest {

    @Test
    fun `one entry is one pane`() {
        assertNull(listPaneOf(Destination.initial))
        assertNull(listPaneOf(listOf(Destination.Settings)))
    }

    @Test
    fun `a forecast on top of a tab pairs with it`() {
        assertEquals(
            Destination.Locations,
            listPaneOf(listOf(Destination.Locations, Destination.Forecast(1L))),
        )
        assertEquals(
            Destination.BestDay,
            listPaneOf(listOf(Destination.BestDay, Destination.Forecast(7L))),
        )
    }

    @Test
    fun `only a forecast is ever the detail`() {
        assertNull(listPaneOf(listOf(Destination.Locations, Destination.Settings)))
    }

    @Test
    fun `a forecast on top of a forecast owns the window`() {
        assertNull(
            listPaneOf(
                listOf(Destination.Locations, Destination.Forecast(1L), Destination.Forecast(2L)),
            ),
        )
    }
}
