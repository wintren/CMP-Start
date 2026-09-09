package com.template.design.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.template.core.ui.resource.StringValue
import com.template.design.preview.AppPreview
import com.template.design.theme.AppTheme

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) = Column(
    modifier = modifier
        .clip(AppTheme.shapes.medium)
        .background(AppTheme.colors.surface)
        .then(border?.let { Modifier.border(it, AppTheme.shapes.medium) } ?: Modifier)
        .then(onClick?.let { Modifier.clickable(onClick = it) } ?: Modifier)
        .padding(AppTheme.spacing.lg),
    content = content,
)

@Composable
fun AppCardShowcase() {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        AppText(StringValue.Raw("Plain surface"), style = AppTheme.typography.subtitle)
    }
    AppCard(modifier = Modifier.fillMaxWidth(), onClick = {}) {
        AppText(StringValue.Raw("Clickable"), style = AppTheme.typography.subtitle)
    }
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(AppTheme.sizing.border, AppTheme.colors.outline),
    ) {
        AppText(StringValue.Raw("Outlined"), style = AppTheme.typography.subtitle)
    }
}

@Preview
@Composable
private fun AppCardLightPreview() = AppPreview { AppCardShowcase() }

@Preview
@Composable
private fun AppCardDarkPreview() = AppPreview(isDark = true) { AppCardShowcase() }
