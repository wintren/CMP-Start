package com.template.app.navigation

/**
 * What a ViewModel is allowed to do to navigation.
 *
 * ViewModels take this interface, never the [Navigator] and never a `NavController`: navigating is
 * then a plain method call a unit test can assert on with a fake, and a screen never has to emit a
 * "navigate" event for someone else to handle.
 */
interface NavControls {
    fun navigateTo(destination: Destination, clearBackStack: Boolean = false)
    fun pop(): Boolean
    fun selectTab(tab: NavTab)
}
