package com.template.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.template.app.navigation.AppNavigation
import com.template.app.navigation.LocalNavControls
import com.template.app.navigation.NavControls
import com.template.app.navigation.NavTab
import com.template.app.navigation.Navigator
import com.template.core.ui.viewmodel.collectState
import com.template.design.theme.AppTheme
import com.template.feature.settings.model.ThemeMode
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

/**
 * The single root every platform renders. Each `:launch:<platform>` module starts Koin and calls
 * this — the entry points hold no UI of their own, so a screen can never behave differently on one
 * platform because someone wired it twice.
 */
@Composable
fun App() {
    val appViewModel: AppViewModel = koinViewModel()
    val appState by appViewModel.collectState()
    val navigator: Navigator = koinInject()
    val navControls: NavControls = koinInject()
    val backStack by navigator.navigation().collectAsState()
    val current = backStack.last()

    val isDark = when (appState.themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    AppTheme(isDark = isDark) {
        CompositionLocalProvider(LocalNavControls provides navControls) {
            Scaffold(
                containerColor = AppTheme.colors.background,
                bottomBar = {
                    // Detail screens are not tabs; the bar would offer a lie about where you are.
                    if (NavTab.of(current) != null) {
                        AppBottomBar(current = current, onSelect = navControls::selectTab)
                    }
                },
            ) { padding ->
                AppNavigation(navigator = navigator, modifier = Modifier.padding(padding))
            }
        }
    }
}
