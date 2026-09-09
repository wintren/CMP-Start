package com.template.design.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.template.design.theme.AppTheme
import com.template.design.theme.AppWindowSize

/**
 * A component rendered outside [AppTheme] reads `LocalAppColors` and throws, so a preview
 * without this wrapper never renders.
 */
@Composable
fun AppPreview(
    isDark: Boolean = false,
    windowSize: AppWindowSize? = null,
    content: @Composable () -> Unit,
) = AppTheme(isDark = isDark, windowSize = windowSize) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .padding(AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        content = { content() },
    )
}
