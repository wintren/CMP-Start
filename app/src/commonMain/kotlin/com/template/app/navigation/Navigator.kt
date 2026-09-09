package com.template.app.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * The back stack, as state the UI observes.
 *
 * A single stack, and [selectTab] resets it to that tab's root: switching tabs discards where you
 * were inside the previous one. Correct for three shallow tabs, wrong the moment a tab has depth
 * worth preserving — at that point give each tab its own stack (a `Stack<T>` per tab, switched by
 * the active tab) rather than trying to make one stack remember several histories.
 *
 * The stack is never empty; [pop] refuses to remove the root so there is no state where nothing
 * renders.
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
}
