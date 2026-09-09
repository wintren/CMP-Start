package com.template.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.template.app.navigation.AppNavigation
import com.template.app.navigation.LocalNavControls
import com.template.app.navigation.NavControls
import com.template.app.navigation.NavTab
import com.template.app.navigation.Navigator
import com.template.app.navigation.listPaneOf
import com.template.core.ui.viewmodel.collectState
import com.template.design.theme.AppTheme
import com.template.design.theme.AppWindowSize
import com.template.feature.settings.model.ThemeMode
import io.ktor.client.HttpClient
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

/**
 * @param windowChrome drawn above the app and inside the theme, so it follows the palette and the
 * user's light/dark choice. `:launch:desktop` puts its own title bar here; the other platforms
 * have OS chrome they do not own.
 */
@Composable
fun App(windowChrome: @Composable () -> Unit = {}) {
    // Coil has no network fetcher on iOS or wasmJs until this runs.
    installAppImageLoader(koinInject<HttpClient>())

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
        val isCompact = AppTheme.windowSize == AppWindowSize.Compact
        val listPane = when (AppTheme.windowSize) {
            AppWindowSize.Expanded -> listPaneOf(backStack)
            else -> null
        }
        // Detail screens are not tabs; the bar would offer a lie about where you are. Beside a
        // list pane the list *is* where you are, so its tab stays selected.
        val activeTab = NavTab.of(listPane ?: current)

        CompositionLocalProvider(LocalNavControls provides navControls) {
            Column(Modifier.fillMaxSize()) {
                windowChrome()

                Scaffold(
                    modifier = Modifier.weight(1f),
                    containerColor = AppTheme.colors.background,
                    bottomBar = {
                        if (isCompact && activeTab != null) {
                            AppBottomBar(active = activeTab, onSelect = navControls::selectTab)
                        }
                    },
                ) { padding ->
                    Row(Modifier.fillMaxSize().padding(padding)) {
                        if (!isCompact && activeTab != null) {
                            AppNavigationRail(active = activeTab, onSelect = navControls::selectTab)
                        }
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            AppNavigation(
                                navigator = navigator,
                                listPane = listPane,
                                // One pane gets a readable line length; two panes divide the
                                // window between themselves and each constrains its own.
                                modifier = when (listPane) {
                                    null -> Modifier.widthIn(max = AppTheme.sizing.contentMaxWidth)
                                    else -> Modifier
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
