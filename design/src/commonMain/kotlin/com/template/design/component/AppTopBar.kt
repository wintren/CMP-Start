package com.template.design.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.template.core.ui.resource.StringValue
import com.template.design.resources.Res
import com.template.design.resources.action_back
import com.template.design.preview.AppPreview
import com.template.design.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: StringValue,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) = TopAppBar(
    title = { AppText(title, style = AppTheme.typography.heading) },
    navigationIcon = {
        onBack?.let {
            IconButton(onClick = it) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.action_back))
            }
        }
    },
    actions = actions,
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = AppTheme.colors.background,
        titleContentColor = AppTheme.colors.textPrimary,
        actionIconContentColor = AppTheme.colors.textSecondary,
        navigationIconContentColor = AppTheme.colors.textSecondary,
    ),
)

@Composable
fun AppTopBarShowcase() {
    AppTopBar(title = StringValue.Raw("Places"))
    AppTopBar(title = StringValue.Raw("Forecast"), onBack = {})
    AppTopBar(
        title = StringValue.Raw("Best day"),
        onBack = {},
        actions = {
            IconButton(onClick = {}) { Icon(Icons.Default.Settings, contentDescription = null) }
        },
    )
}

@Preview
@Composable
private fun AppTopBarLightPreview() = AppPreview { AppTopBarShowcase() }

@Preview
@Composable
private fun AppTopBarDarkPreview() = AppPreview(isDark = true) { AppTopBarShowcase() }
