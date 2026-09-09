package com.template.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.template.app.navigation.entries.settingsEntries
import com.template.app.navigation.entries.weatherEntries

@Composable
fun AppNavigation(
    navigator: Navigator,
    listPane: Destination?,
    modifier: Modifier = Modifier,
) {
    val backStack by navigator.navigation().collectAsState()

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { navigator.pop() },
        sceneStrategies = listOf(ListDetailSceneStrategy(listPane)),
        transitionSpec = { forwardTransition() },
        popTransitionSpec = { backwardTransition() },
        predictivePopTransitionSpec = { backwardTransition() },
        entryProvider = entryProvider {
            weatherEntries()
            settingsEntries()
        },
    )
}
