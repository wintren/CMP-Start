package com.template.app

import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import com.template.app.navigation.NavTab
import com.template.design.component.AppIcon
import com.template.design.component.AppText
import com.template.design.theme.AppTheme

/** The bottom bar's other form, from `Medium` up. Same tabs, same selection. */
@Composable
fun AppNavigationRail(active: NavTab, onSelect: (NavTab) -> Unit) {
    NavigationRail(containerColor = AppTheme.colors.surface) {
        NavTab.entries.forEach { tab ->
            NavigationRailItem(
                selected = tab == active,
                onClick = { onSelect(tab) },
                icon = { AppIcon(tab.icon, description = null) },
                label = { AppText(tab.label, style = AppTheme.typography.label) },
                colors = NavigationRailItemDefaults.colors(
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
