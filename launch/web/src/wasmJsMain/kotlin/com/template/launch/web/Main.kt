package com.template.launch.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.template.app.App
import com.template.app.di.startAppKoin
import com.template.app.navigation.Navigator
import kotlinx.coroutines.MainScope
import org.koin.mp.KoinPlatform

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startAppKoin()
    MainScope().bindBrowserHistory(KoinPlatform.getKoin().get<Navigator>())
    ComposeViewport { App() }
}
