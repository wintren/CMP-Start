package com.template.app.weather.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.template.core.ui.resource.StringValue
import com.template.design.component.AppText
import com.template.design.theme.AppTheme
import com.template.domain.weather.model.ComfortScore

@Composable
fun ScoreBadge(score: ComfortScore, modifier: Modifier = Modifier) {
    val background = when {
        score.value >= GOOD -> AppTheme.colors.success
        score.value >= FAIR -> AppTheme.colors.warning
        else -> AppTheme.colors.error
    }
    Box(
        modifier = modifier
            .clip(AppTheme.shapes.pill)
            .background(background)
            .padding(horizontal = AppTheme.spacing.md, vertical = AppTheme.spacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        AppText(
            StringValue.of(score.value),
            style = AppTheme.typography.label,
            color = Color.White,
        )
    }
}

private const val GOOD = ComfortScore.GOOD_THRESHOLD
private const val FAIR = 45
