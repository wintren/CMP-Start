package com.template.app.navigation

interface NavControls {
    fun navigateTo(destination: Destination, clearBackStack: Boolean = false)
    fun pop(): Boolean
    fun selectTab(tab: NavTab)
}
