package com.template.app.weather.bestday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.template.app.resources.Res
import com.template.app.resources.best_day_activity
import com.template.app.resources.best_day_empty_body
import com.template.app.resources.best_day_empty_title
import com.template.app.resources.action_refresh
import com.template.app.resources.best_day_title
import com.template.app.weather.bestday.BestDayModels.Action
import com.template.app.weather.bestday.BestDayModels.RankedItem
import com.template.app.weather.bestday.BestDayModels.State
import com.template.app.weather.component.PenaltyChips
import com.template.app.weather.component.ScoreBadge
import com.template.app.weather.format.icon
import com.template.app.weather.format.label
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
import com.template.design.component.AppCard
import com.template.design.component.AppIcon
import com.template.design.component.AppText
import com.template.design.component.AppTopBar
import com.template.design.component.feedback.EmptyView
import com.template.design.component.feedback.ErrorView
import com.template.design.theme.AppTheme
import com.template.design.theme.AppWindowSize

@Composable
fun BestDayScreen(
    state: State,
    onAction: (Action) -> Unit,
) = Scaffold(
    topBar = {
        AppTopBar(title = Res.string.best_day_title.asValue()) {
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
        AppText(
            Res.string.best_day_activity.asValue(),
            style = AppTheme.typography.label,
            color = AppTheme.colors.textSecondary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            state.activities.forEach { activity ->
                FilterChip(
                    selected = activity == state.activity,
                    onClick = { onAction(Action.OnActivityChange(activity)) },
                    label = { AppText(activity.label()) },
                )
            }
        }

        state.error?.let { ErrorView(message = it, modifier = Modifier.fillMaxWidth()) }

        when {
            state.ranked.isEmpty() -> EmptyView(
                title = Res.string.best_day_empty_title.asValue(),
                body = Res.string.best_day_empty_body.asValue(),
                icon = Icons.Default.Star,
            )

            // The pane's own width, not the window's: this list is 360dp wide when it sits
            // beside a forecast, and two columns would not fit in it.
            else -> BoxWithConstraints {
                val columns = when (AppWindowSize.of(maxWidth)) {
                    AppWindowSize.Expanded -> 2
                    else -> 1
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                ) {
                    items(state.ranked, key = { it.key }) { item ->
                        RankedRow(item) { onAction(Action.OnOpenPlace(it)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun RankedRow(item: RankedItem, onOpen: (Long) -> Unit) = AppCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = { onOpen(item.locationId) },
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        modifier = Modifier.fillMaxWidth(),
    ) {
        ScoreBadge(item.score)
        Column(Modifier.weight(1f)) {
            AppText(item.place, style = AppTheme.typography.subtitle)
            AppText(
                item.dayLabel,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.textSecondary,
            )
        }
        // The only place this row names the weather, so it is not decorative.
        AppIcon(
            icon = item.condition.icon(),
            description = item.condition.label(),
            tint = AppTheme.colors.textSecondary,
            size = AppTheme.sizing.iconSmall,
        )
        AppText(item.temperature, style = AppTheme.typography.heading)
    }
    PenaltyChips(item.penalties, modifier = Modifier.padding(top = AppTheme.spacing.sm))
}
