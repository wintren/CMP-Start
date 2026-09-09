package com.template.launch.desktop

import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.template.app.App
import com.template.app.di.startAppKoin
import java.awt.Taskbar
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

private const val APP_NAME = "CMP Start"

/** Optional: drop a PNG here and the Dock, the taskbar and the window all pick it up. */
private const val ICON_RESOURCE = "desktop_icon.png"

fun main() {
    // Before AWT starts, or the Dock keeps the main class name and the system chrome stays light.
    System.setProperty("apple.awt.application.name", APP_NAME)
    System.setProperty("apple.awt.application.appearance", "system")

    startAppKoin()

    val icon = loadAppIcon()
    if (icon != null) applyDockIcon(icon)

    application {
        Window(
            // Wide enough to open in the two-pane layout; drag it narrow to see it collapse.
            state = rememberWindowState(size = DpSize(width = 1280.dp, height = 860.dp)),
            onCloseRequest = ::exitApplication,
            title = APP_NAME,
            icon = icon?.toPainter(),
        ) {
            App(windowChrome = { AppTitleBar(APP_NAME) })
        }
    }
}

private fun loadAppIcon(): BufferedImage? = runCatching {
    Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(ICON_RESOURCE)
        ?.use { ImageIO.read(it) }
}.getOrNull()

private fun applyDockIcon(image: BufferedImage) {
    runCatching {
        if (!Taskbar.isTaskbarSupported()) return
        val taskbar = Taskbar.getTaskbar()
        if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            taskbar.iconImage = image
        }
    }
}
