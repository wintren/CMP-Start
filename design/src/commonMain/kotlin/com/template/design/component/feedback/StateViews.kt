package com.template.design.component.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.template.core.ui.resource.StringValue
import com.template.design.component.AppButton
import com.template.design.component.AppButtonVariant
import com.template.design.component.AppText
import com.template.design.theme.AppTheme

@Composable
fun LoadingView(modifier: Modifier = Modifier.fillMaxSize()) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    CircularProgressIndicator(color = AppTheme.colors.primary)
}

@Composable
fun EmptyView(
    title: StringValue,
    body: StringValue,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier.fillMaxSize(),
    action: Pair<StringValue, () -> Unit>? = null,
) = Box(modifier = modifier, contentAlignment = Alignment.Center) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        modifier = Modifier.padding(AppTheme.spacing.xl),
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = AppTheme.colors.textDisabled,
            )
        }
        AppText(title, style = AppTheme.typography.heading, textAlign = TextAlign.Center)
        AppText(
            body,
            style = AppTheme.typography.body,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        action?.let { (label, onClick) ->
            AppButton(label = label, onClick = onClick, variant = AppButtonVariant.Secondary)
        }
    }
}

@Composable
fun ErrorView(
    message: StringValue,
    onRetry: (() -> Unit)? = null,
    retryLabel: StringValue = StringValue.Raw("Retry"),
    modifier: Modifier = Modifier.fillMaxSize(),
) = Box(modifier = modifier, contentAlignment = Alignment.Center) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        modifier = Modifier.padding(AppTheme.spacing.xl),
    ) {
        AppText(
            message,
            style = AppTheme.typography.body,
            color = AppTheme.colors.error,
            textAlign = TextAlign.Center,
        )
        onRetry?.let {
            AppButton(label = retryLabel, onClick = it, variant = AppButtonVariant.Secondary)
        }
    }
}
