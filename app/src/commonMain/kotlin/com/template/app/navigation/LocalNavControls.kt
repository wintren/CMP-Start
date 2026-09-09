package com.template.app.navigation

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * For the few navigations a composable genuinely owns (the bottom bar). Screen content navigates
 * by dispatching an Action to its ViewModel, which calls [NavControls] — not through this.
 */
val LocalNavControls = staticCompositionLocalOf<NavControls> {
    error("NavControls not provided")
}
