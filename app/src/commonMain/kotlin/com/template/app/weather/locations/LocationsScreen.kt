package com.template.app.weather.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.template.app.weather.format.icon
import com.template.app.weather.locations.LocationsModels.Action
import com.template.app.weather.locations.LocationsModels.PlaceItem
import com.template.app.weather.locations.LocationsModels.SavedItem
import com.template.app.weather.locations.LocationsModels.State
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
import com.template.design.component.AppCard
import com.template.design.component.AppIcon
import com.template.design.component.AppText
import com.template.design.component.AppTextField
import com.template.design.component.AppTopBar
import com.template.design.component.feedback.EmptyView
import com.template.design.component.feedback.ErrorView
import com.template.design.theme.AppTheme
import com.template.app.resources.Res
import com.template.app.resources.action_dismiss
import com.template.app.resources.locations_empty_body
import com.template.app.resources.locations_empty_title
import com.template.app.resources.locations_search_placeholder
import com.template.app.resources.action_add_place
import com.template.app.resources.action_refresh
import com.template.app.resources.action_remove_place
import com.template.app.resources.locations_title

@Composable
fun LocationsScreen(
    state: State,
    onAction: (Action) -> Unit,
) = Scaffold(
    topBar = {
        AppTopBar(title = Res.string.locations_title.asValue()) {
            IconButton(onClick = { onAction(Action.OnRefresh) }) {
                AppIcon(Icons.Default.Refresh, description = Res.string.action_refresh.asValue())
            }
        }
    },
    containerColor = AppTheme.colors.background,
) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
    ) {
        AppTextField(
            value = state.query,
            onValueChange = { onAction(Action.OnQueryChange(it)) },
            placeholder = Res.string.locations_search_placeholder.asValue(),
            onSubmit = { onAction(Action.OnSearchSubmit) },
        )

        state.error?.let { message ->
            ErrorView(
                message = message,
                onRetry = { onAction(Action.OnDismissError) },
                retryLabel = Res.string.action_dismiss.asValue(),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        when {
            state.isSearching -> Row(
                Modifier.fillMaxWidth().padding(AppTheme.spacing.lg),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(Modifier.size(AppTheme.sizing.icon), color = AppTheme.colors.primary)
            }

            state.showsSearchResults -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            ) {
                items(state.searchResults, key = { it.id }) { place ->
                    PlaceRow(place) { onAction(Action.OnAddPlace(place.id)) }
                }
            }

            state.saved.isEmpty() -> EmptyView(
                title = Res.string.locations_empty_title.asValue(),
                body = Res.string.locations_empty_body.asValue(),
                icon = Icons.Default.Search,
            )

            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    bottom = AppTheme.spacing.xl,
                ),
            ) {
                items(state.saved, key = { it.id }) { saved ->
                    SavedRow(
                        item = saved,
                        onOpen = { onAction(Action.OnOpenSaved(saved.id)) },
                        onRemove = { onAction(Action.OnRemoveSaved(saved.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceRow(place: PlaceItem, onAdd: () -> Unit) = AppCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = onAdd,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            AppText(place.name, style = AppTheme.typography.subtitle)
            AppText(
                place.region,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.textSecondary,
            )
        }
        AppIcon(
            Icons.Default.Add,
            description = Res.string.action_add_place.asValue(),
            tint = AppTheme.colors.primary,
        )
    }
}

@Composable
private fun SavedRow(item: SavedItem, onOpen: () -> Unit, onRemove: () -> Unit) = AppCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = onOpen,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        modifier = Modifier.fillMaxWidth(),
    ) {
        // Decorative: `conditionLabel` says the same thing in words, two columns over.
        AppIcon(
            icon = item.condition.icon(),
            description = null,
            tint = AppTheme.colors.accent,
            size = AppTheme.sizing.iconLarge,
        )
        Column(Modifier.weight(1f)) {
            AppText(item.name, style = AppTheme.typography.subtitle)
            AppText(
                item.region,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.textSecondary,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            AppText(item.temperature, style = AppTheme.typography.heading)
            AppText(
                item.conditionLabel,
                style = AppTheme.typography.label,
                color = AppTheme.colors.textSecondary,
            )
        }
        IconButton(onClick = onRemove) {
            AppIcon(
                Icons.Default.Delete,
                description = Res.string.action_remove_place.asValue(),
                tint = AppTheme.colors.textDisabled,
            )
        }
    }
}
