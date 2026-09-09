@file:Suppress("FunctionName", "unused")

package com.template.launch.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.template.app.App
import com.template.app.di.startAppKoin

/**
 * Called from Swift: `MainViewControllerKt.MainViewController()`.
 *
 * This module builds `AppFramework.framework`; add it to an Xcode project and return this
 * controller from a `UIViewControllerRepresentable`.
 */
fun MainViewController() = ComposeUIViewController {
    startAppKoin()
    App()
}
