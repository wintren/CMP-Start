package com.template.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
import com.template.design.component.AppCard
import com.template.design.component.AppText
import com.template.design.component.AppTopBar
import com.template.design.theme.AppTheme
import com.template.feature.settings.SettingsModels.Action
import com.template.feature.settings.SettingsModels.State
import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem
import com.template.feature.settings.resources.Res
import com.template.feature.settings.resources.settings_example
import com.template.feature.settings.resources.settings_theme
import com.template.feature.settings.resources.settings_title
import com.template.feature.settings.resources.settings_units

/**
 * `state` in, `onAction` out. No ViewModel, no Koin, no coroutines — so this renders in a preview
 * and in a screenshot test by constructing a [State].
 */
@Composable
fun SettingsScreen(
    state: State,
    onAction: (Action) -> Unit,
) = Scaffold(
    topBar = {
        AppTopBar(
            title = Res.string.settings_title.asValue(),
            onBack = { onAction(Action.OnBack) },
        )
    },
    containerColor = AppTheme.colors.background,
) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
    ) {
        ChoiceCard(
            title = Res.string.settings_units.asValue(),
            options = state.unitOptions,
            selected = state.unitSystem,
            onSelect = { onAction(Action.OnUnitSystemChange(it)) },
        )

        ChoiceCard(
            title = Res.string.settings_theme.asValue(),
            options = state.themeOptions,
            selected = state.themeMode,
            onSelect = { onAction(Action.OnThemeModeChange(it)) },
        )

        AppCard {
            AppText(
                Res.string.settings_example.asValue(),
                style = AppTheme.typography.label,
                color = AppTheme.colors.textSecondary,
            )
            AppText(state.temperatureExample, style = AppTheme.typography.subtitle)
        }
    }
}

@Composable
private fun <T> ChoiceCard(
    title: StringValue,
    options: List<SettingsModels.Option<T>>,
    selected: T,
    onSelect: (T) -> Unit,
) = AppCard(modifier = Modifier.fillMaxWidth()) {
    AppText(title, style = AppTheme.typography.heading)
    Column(Modifier.selectableGroup()) {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                RadioButton(
                    selected = option.value == selected,
                    onClick = { onSelect(option.value) },
                )
                AppText(option.label, style = AppTheme.typography.body)
            }
        }
    }
}
