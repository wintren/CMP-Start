package com.template.launch.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.template.core.ui.resource.StringValue
import com.template.design.component.AppText
import com.template.design.theme.AppTheme

/**
 * macOS only. `fullWindowContent` lets the app draw all the way to the top of the window and the
 * system keeps the traffic lights floating above it, so there is one bar instead of two — which is
 * also why the bar starts [TRAFFIC_LIGHT_INSET] in.
 *
 * Windows and Linux keep their native decoration: drawing our own there means re-implementing
 * minimise, maximise, snap and double-click-to-zoom, and getting one of them wrong is worse than
 * a bar that looks like every other window on the desktop.
 */
@Composable
fun FrameWindowScope.AppTitleBar(title: String) {
    if (!isMacOs) return

    LaunchedEffect(Unit) { window.hideSystemTitleBar() }

    WindowDraggableArea {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TITLE_BAR_HEIGHT)
                .background(AppTheme.colors.surface)
                .padding(start = TRAFFIC_LIGHT_INSET, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppText(
                StringValue.Raw(title),
                style = AppTheme.typography.label,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}

internal val isMacOs: Boolean =
    System.getProperty("os.name").orEmpty().contains("mac", ignoreCase = true)

/** Set on the realised window: AWT ignores them if they arrive before the peer exists. */
private fun ComposeWindow.hideSystemTitleBar() {
    rootPane.putClientProperty("apple.awt.fullWindowContent", true)
    rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
    rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
}

private val TITLE_BAR_HEIGHT = 40.dp
private val TRAFFIC_LIGHT_INSET = 84.dp
