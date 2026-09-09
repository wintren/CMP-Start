package com.template.launch.web

import com.template.app.navigation.Destination
import com.template.app.navigation.Navigator
import com.template.app.navigation.destinationOf
import com.template.app.navigation.stackFor
import com.template.app.navigation.toRoute
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * On the web the address bar *is* the back stack. Without this the browser's back button leaves
 * the app, a reload always lands on the first screen, and no screen can be linked to.
 *
 * Only the destination on top goes in the URL, so a link stays short and shareable; [stackFor]
 * gives it something to go back to. Assigning `location.hash` rather than pushing history entries
 * by hand keeps this to one API and needs no server-side rewrite for deep links to work.
 */
fun CoroutineScope.bindBrowserHistory(navigator: Navigator) {
    openedDestination()?.let { navigator.restore(stackFor(it)) }

    window.addEventListener("hashchange") {
        val opened = openedDestination() ?: return@addEventListener
        // Ignore the change we caused ourselves in the collector below.
        if (opened.toRoute() != navigator.navigation().value.last().toRoute()) {
            navigator.restore(stackFor(opened))
        }
    }

    launch {
        navigator.navigation().collect { stack ->
            val route = stack.last().toRoute()
            if (currentRoute() != route) window.location.hash = "/$route"
        }
    }
}

private fun currentRoute(): String = window.location.hash.removePrefix("#").trim('/')

private fun openedDestination(): Destination? = destinationOf(currentRoute())
