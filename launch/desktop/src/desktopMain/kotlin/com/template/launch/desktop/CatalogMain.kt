package com.template.launch.desktop

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.template.app.catalog.AppCatalog
import com.template.app.di.startAppKoin

/**
 * Second entry point for the same desktop module: `./gradlew :launch:desktop:run -PappCatalog`.
 *
 * Koin still starts, because the catalog shows `AppImage` and that needs the app's HTTP client.
 */
fun main() {
    startAppKoin()

    application {
        Window(
            state = rememberWindowState(size = DpSize(width = 1100.dp, height = 900.dp)),
            onCloseRequest = ::exitApplication,
            title = "CMP Start — Catalog",
        ) {
            AppCatalog()
        }
    }
}
