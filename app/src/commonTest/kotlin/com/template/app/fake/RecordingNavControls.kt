package com.template.app.fake

import com.template.app.navigation.Destination
import com.template.app.navigation.NavControls
import com.template.app.navigation.NavTab

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
