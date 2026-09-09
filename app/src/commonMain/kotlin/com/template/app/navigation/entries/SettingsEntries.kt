package com.template.app.navigation.entries

import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import com.template.app.navigation.Destination
import com.template.app.navigation.NavControls
import com.template.core.ui.viewmodel.collectState
import com.template.feature.settings.SettingsScreen
import com.template.feature.settings.SettingsViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/** The feature cannot know this app's navigator, so the host passes `onBack` in. */
fun EntryProviderScope<Destination>.settingsEntries() {

    entry<Destination.Settings> {
        val navControls: NavControls = koinInject()
        val viewModel: SettingsViewModel = koinViewModel { parametersOf({ navControls.pop() }) }
        val state by viewModel.collectState()
        SettingsScreen(state = state, onAction = viewModel::onAction)
    }
}
