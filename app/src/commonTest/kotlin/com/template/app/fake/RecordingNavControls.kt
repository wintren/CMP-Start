package com.template.app.fake

import com.template.app.navigation.Destination
import com.template.app.navigation.NavControls
import com.template.app.navigation.NavTab

/**
 * Navigation as an assertable list. This is the payoff of injecting [NavControls] into ViewModels
 * rather than emitting navigation as an event for the UI to interpret: "tapping this opens that"
 * is a plain unit test.
 */
class RecordingNavControls : NavControls {

    val navigated = mutableListOf<Destination>()
    var popCount: Int = 0
        private set

    override fun navigateTo(destination: Destination, clearBackStack: Boolean) {
        navigated += destination
    }

    override fun pop(): Boolean {
        popCount++
        return true
    }

    override fun selectTab(tab: NavTab) {
        navigated += tab.destination
    }
}
