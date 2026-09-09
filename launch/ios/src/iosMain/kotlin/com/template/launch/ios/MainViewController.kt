@file:Suppress("FunctionName", "unused")

package com.template.launch.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.template.app.App
import com.template.app.di.startAppKoin

/** Called from Swift as `MainViewControllerKt.MainViewController()`. */
fun MainViewController() = ComposeUIViewController {
    startAppKoin()
    App()
}
