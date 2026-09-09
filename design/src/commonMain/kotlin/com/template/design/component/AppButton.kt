package com.template.design.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.template.core.ui.resource.StringValue
import com.template.design.theme.AppTheme

enum class AppButtonVariant { Primary, Secondary, Text, Danger }

@Composable
fun AppButton(
    label: StringValue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AppButtonVariant = AppButtonVariant.Primary,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val content: @Composable () -> Unit = {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let { Icon(it, contentDescription = null, modifier = Modifier.size(18.dp)) }
            AppText(label, style = AppTheme.typography.subtitle, color = androidx.compose.ui.graphics.Color.Unspecified)
        }
    }
    val padding = PaddingValues(horizontal = AppTheme.spacing.lg, vertical = AppTheme.spacing.md)

    when (variant) {
        AppButtonVariant.Primary -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = AppTheme.shapes.medium,
            contentPadding = padding,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.primary,
                contentColor = AppTheme.colors.onPrimary,
            ),
        ) { content() }

        AppButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = AppTheme.shapes.medium,
            contentPadding = padding,
            border = BorderStroke(1.dp, AppTheme.colors.outline),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AppTheme.colors.textPrimary),
        ) { content() }

        AppButtonVariant.Text -> TextButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            contentPadding = padding,
            colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.primary),
        ) { content() }

        AppButtonVariant.Danger -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = AppTheme.shapes.medium,
            contentPadding = padding,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.error,
                contentColor = AppTheme.colors.onError,
            ),
        ) { content() }
    }
}
