package com.template.launch.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.template.app.App
import com.template.app.di.startAppKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startAppKoin()
    ComposeViewport { App() }
}
