package com.template.design.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.template.design.theme.AppTheme

/**
 * The wrapper every `@Preview` in `:design` uses: a component rendered outside [AppTheme] reads
 * `LocalAppColors` and throws, so a preview without this is a preview that never renders.
 *
 * Pass `isDark` to see the other half of the palette. Every component carries at least one
 * preview — see the rule in docs/architecture.md.
 */
@Composable
fun AppPreview(
    isDark: Boolean = false,
    content: @Composable () -> Unit,
) = AppTheme(isDark = isDark) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .padding(AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        content = { content() },
    )
}
