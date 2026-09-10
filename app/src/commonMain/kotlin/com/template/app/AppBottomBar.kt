package com.template.app

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import com.template.app.navigation.NavTab
import com.template.design.component.AppIcon
import com.template.design.component.AppText
import com.template.design.theme.AppTheme

@Composable
fun AppBottomBar(active: NavTab, onSelect: (NavTab) -> Unit) {
    NavigationBar(containerColor = AppTheme.colors.surface) {
        NavTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = tab == active,
                onClick = { onSelect(tab) },
                icon = { AppIcon(tab.icon, description = null) },
                label = { AppText(tab.label, style = AppTheme.typography.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppTheme.colors.primary,
                    selectedTextColor = AppTheme.colors.primary,
                    unselectedIconColor = AppTheme.colors.textSecondary,
                    unselectedTextColor = AppTheme.colors.textSecondary,
                    indicatorColor = AppTheme.colors.surfaceRaised,
                ),
            )
        }
    }
}
