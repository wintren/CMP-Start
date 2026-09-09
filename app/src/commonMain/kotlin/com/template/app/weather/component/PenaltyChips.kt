package com.template.app.weather.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.template.app.weather.format.label
import com.template.design.component.AppText
import com.template.design.theme.AppTheme
import com.template.domain.weather.model.ComfortPenalty

/** Why a day scored what it did. Reading the reasons is the point of returning them from `logic`. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PenaltyChips(penalties: List<ComfortPenalty>, modifier: Modifier = Modifier) {
    if (penalties.isEmpty()) return
    FlowRow(
        modifier = modifier,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(AppTheme.spacing.sm),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        penalties.forEach { penalty ->
            AppText(
                text = penalty.label(),
                style = AppTheme.typography.label,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier
                    .clip(AppTheme.shapes.pill)
                    .border(1.dp, AppTheme.colors.outline, AppTheme.shapes.pill)
                    .padding(horizontal = AppTheme.spacing.sm, vertical = 2.dp),
            )
        }
    }
}
