package com.template.design.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.template.core.ui.resource.StringValue
import com.template.design.theme.AppTheme

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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
