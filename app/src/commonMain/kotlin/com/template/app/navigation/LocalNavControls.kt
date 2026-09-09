package com.template.app.navigation

import androidx.compose.runtime.staticCompositionLocalOf

/** For the few navigations a composable owns (the bottom bar). Screens dispatch an Action. */
val LocalNavControls = staticCompositionLocalOf<NavControls> {
    error("NavControls not provided")
}
