package com.template.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.resolve
import com.template.design.preview.AppPreview
import com.template.design.theme.AppTheme

/**
 * The only icon in the app, so that a description is a decision somebody made rather than a
 * parameter they left out.
 *
 * @param description what a screen reader reads out. `null` is the right answer when the icon
 * repeats a label next to it or is pure decoration, and the wrong answer whenever the icon is the
 * only place some information appears — a weather icon in a row that names no condition.
 */
@Composable
fun AppIcon(
    icon: ImageVector,
    description: StringValue?,
    modifier: Modifier = Modifier,
    size: Dp = AppTheme.sizing.icon,
    tint: Color = LocalContentColor.current,
) = Icon(
    imageVector = icon,
    contentDescription = description?.resolve(),
    modifier = modifier.size(size),
    tint = tint,
)

@Composable
fun AppIconShowcase() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
        verticalAlignment = Alignment.Bottom,
    ) {
        listOf(
            "iconInline" to AppTheme.sizing.iconInline,
            "iconSmall" to AppTheme.sizing.iconSmall,
            "icon" to AppTheme.sizing.icon,
            "iconLarge" to AppTheme.sizing.iconLarge,
            "iconDisplay" to AppTheme.sizing.iconDisplay,
        ).forEach { (role, size) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
            ) {
                AppIcon(Icons.Default.Cloud, description = null, size = size)
                AppText(StringValue.Raw(role), style = AppTheme.typography.label)
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(
            Icons.Default.Refresh,
            description = StringValue.Raw("Described: a screen reader reads this"),
            tint = AppTheme.colors.primary,
        )
        AppIcon(Icons.Default.Delete, description = null, tint = AppTheme.colors.textDisabled)
        AppText(StringValue.Raw("tinted, described and decorative"), style = AppTheme.typography.bodySmall)
    }
}

@Preview
@Composable
private fun AppIconLightPreview() = AppPreview { AppIconShowcase() }

@Preview
@Composable
private fun AppIconDarkPreview() = AppPreview(isDark = true) { AppIconShowcase() }
