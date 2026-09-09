package com.template.app.weather.forecast

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.template.app.resources.Res
import com.template.app.resources.forecast_missing
import com.template.app.weather.forecast.ForecastModels.Action
import com.template.app.weather.forecast.ForecastModels.CurrentBlock
import com.template.app.weather.forecast.ForecastModels.DayItem
import com.template.app.weather.forecast.ForecastModels.State
import com.template.app.weather.format.icon
import com.template.core.ui.resource.asValue
import com.template.design.component.AppCard
import com.template.design.component.AppText
import com.template.design.component.AppTopBar
import com.template.design.component.feedback.ErrorView
import com.template.design.component.feedback.LoadingView
import com.template.design.theme.AppTheme

@Composable
fun ForecastScreen(
    state: State,
    onAction: (Action) -> Unit,
) = Scaffold(
    topBar = {
        AppTopBar(title = state.title, onBack = { onAction(Action.OnBack) }) {
            IconButton(onClick = { onAction(Action.OnRefresh) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
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
        state.error?.let { ErrorView(message = it, modifier = Modifier.fillMaxWidth()) }

        when {
            state.isLoading && state.error == null -> LoadingView()
            state.current == null -> ErrorView(message = Res.string.forecast_missing.asValue())
            else -> {
                CurrentCard(state.current, state.region)
                LazyColumn(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
                    items(state.days, key = { it.id }) { DayRow(it) }
                }
            }
        }
    }
}

@Composable
private fun CurrentCard(
    current: CurrentBlock,
    region: com.template.core.ui.resource.StringValue,
) = AppCard(modifier = Modifier.fillMaxWidth()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = current.condition.icon(),
            contentDescription = null,
            tint = AppTheme.colors.accent,
            modifier = Modifier.size(44.dp),
        )
        Column(Modifier.weight(1f)) {
            AppText(current.temperature, style = AppTheme.typography.numeric)
            AppText(
                current.conditionLabel,
                style = AppTheme.typography.body,
                color = AppTheme.colors.textSecondary,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            AppText(current.wind, style = AppTheme.typography.subtitle)
            AppText(
                region,
                style = AppTheme.typography.label,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun DayRow(day: DayItem) = AppCard(modifier = Modifier.fillMaxWidth()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        modifier = Modifier.fillMaxWidth(),
    ) {
        AppText(day.dayLabel, style = AppTheme.typography.subtitle, modifier = Modifier.weight(1f))
        Icon(
            imageVector = day.condition.icon(),
            contentDescription = null,
            tint = AppTheme.colors.textSecondary,
            modifier = Modifier.size(20.dp),
        )
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
            AppText(day.precipitationChance, style = AppTheme.typography.label, color = AppTheme.colors.primary)
            AppText(day.wind, style = AppTheme.typography.label, color = AppTheme.colors.textSecondary)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            AppText(day.high, style = AppTheme.typography.subtitle)
            AppText(day.low, style = AppTheme.typography.subtitle, color = AppTheme.colors.textDisabled)
        }
    }
}
