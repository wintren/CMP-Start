package com.template.app

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import com.template.app.navigation.Destination
import com.template.app.navigation.NavTab
import com.template.core.ui.resource.StringValue
import com.template.design.component.AppText
import com.template.design.theme.AppTheme

@Composable
fun AppBottomBar(current: Destination, onSelect: (NavTab) -> Unit) {
    val activeTab = NavTab.of(current)
    NavigationBar(containerColor = AppTheme.colors.surface) {
        NavTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = tab == activeTab,
                onClick = { onSelect(tab) },
                icon = { Icon(tab.icon, contentDescription = null) },
                label = { AppText(StringValue.Raw(tab.label), style = AppTheme.typography.label) },
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
