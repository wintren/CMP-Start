package com.template.launch.desktop

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.template.app.App
import com.template.app.di.startAppKoin

fun main() {
    startAppKoin()

    application {
        Window(
            state = rememberWindowState(size = DpSize(width = 420.dp, height = 860.dp)),
            onCloseRequest = ::exitApplication,
            title = "CMP Start",
        ) {
            App()
        }
    }
}
