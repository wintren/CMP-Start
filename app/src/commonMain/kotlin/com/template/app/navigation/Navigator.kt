package com.template.app.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * One stack, and [selectTab] resets it to that tab's root — switching tabs discards where you
 * were. Correct for three shallow tabs; give each tab its own stack once one has depth.
 */
class Navigator : NavControls {

    private val stack = MutableStateFlow(Destination.initial)

    fun navigation(): StateFlow<List<Destination>> = stack.asStateFlow()

    override fun navigateTo(destination: Destination, clearBackStack: Boolean) {
        stack.update { current ->
            when {
                clearBackStack -> listOf(destination)
                current.lastOrNull() == destination -> current
                else -> current + destination
            }
        }
    }

    override fun pop(): Boolean {
        var popped = false
        stack.update { current ->
            if (current.size > 1) {
                popped = true
                current.dropLast(1)
            } else {
                current
            }
        }
        return popped
    }

    override fun selectTab(tab: NavTab) = navigateTo(tab.destination, clearBackStack = true)

    /** The host's entry point: a restored stack, or a deep link's [stackFor]. Never a screen's. */
    fun restore(destinations: List<Destination>) {
        if (destinations.isEmpty()) return
        stack.value = destinations
    }

    /**
     * True until something has navigated. A link someone opened just now has already moved us, and
     * must not be overwritten by whatever the last session happened to save.
     */
    val isAtStart: Boolean get() = stack.value == Destination.initial
}
