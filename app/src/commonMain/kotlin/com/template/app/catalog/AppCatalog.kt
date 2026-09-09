package com.template.app.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.template.app.installAppImageLoader
import com.template.core.ui.resource.StringValue
import com.template.design.component.AppButton
import com.template.design.component.AppButtonShowcase
import com.template.design.component.AppButtonVariant
import com.template.design.component.AppCardShowcase
import com.template.design.component.AppImageShowcase
import com.template.design.component.AppText
import com.template.design.component.AppTextFieldShowcase
import com.template.design.component.AppTextShowcase
import com.template.design.component.AppTopBarShowcase
import com.template.design.component.feedback.StateViewsShowcase
import com.template.design.theme.AppColors
import com.template.design.theme.AppTheme
import com.template.design.theme.AppWindowSize
import io.ktor.client.HttpClient
import org.koin.compose.koinInject

/**
 * Every design token and component on one screen, in both palettes.
 * `./gradlew :launch:desktop:run -PappCatalog`. Never a nav destination, so R8 drops it.
 */
@Composable
fun AppCatalog() {
    installAppImageLoader(koinInject<HttpClient>())

    var isDark by remember { mutableStateOf(false) }
    var section by remember { mutableStateOf(CatalogSection.Colors) }

    AppTheme(isDark = isDark) {
        Row(modifier = Modifier.fillMaxSize().background(AppTheme.colors.background)) {
            Sidebar(
                selected = section,
                isDark = isDark,
                onSelect = { section = it },
                onToggleTheme = { isDark = !isDark },
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(AppTheme.sizing.border)
                    .background(AppTheme.colors.outline),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(AppTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
            ) {
                when (section) {
                    CatalogSection.Colors -> ColorsSection(isDark = isDark)
                    CatalogSection.Typography -> AppTextShowcase()
                    CatalogSection.Metrics -> MetricsSection()
                    CatalogSection.Components -> ComponentsSection()
                }
            }
        }
    }
}

enum class CatalogSection(val label: String) {
    Colors("Colours"),
    Typography("Typography"),
    Metrics("Spacing & sizing"),
    Components("Components"),
}

private val SIDEBAR_WIDTH = 190.dp
private val SWATCH_SIZE = 64.dp
private val SWATCH_LABEL_WIDTH = 132.dp
private val METRIC_BAR_HEIGHT = 12.dp
private val METRIC_LABEL_WIDTH = 110.dp

@Composable
private fun Sidebar(
    selected: CatalogSection,
    isDark: Boolean,
    onSelect: (CatalogSection) -> Unit,
    onToggleTheme: () -> Unit,
) = Column(
    modifier = Modifier
        .width(SIDEBAR_WIDTH)
        .fillMaxHeight()
        .padding(vertical = AppTheme.spacing.lg),
    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
) {
    AppText(
        StringValue.Raw("Catalog"),
        style = AppTheme.typography.heading,
        modifier = Modifier.padding(horizontal = AppTheme.spacing.lg),
    )
    Spacer(Modifier.height(AppTheme.spacing.md))

    CatalogSection.entries.forEach { entry ->
        val isSelected = entry == selected
        AppText(
            StringValue.Raw(entry.label),
            style = AppTheme.typography.body,
            color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelect(entry) }
                .background(
                    if (isSelected) AppTheme.colors.surfaceRaised else Color.Transparent,
                )
                .padding(horizontal = AppTheme.spacing.lg, vertical = AppTheme.spacing.md),
        )
    }

    Spacer(Modifier.weight(1f))
    AppButton(
        label = StringValue.Raw(if (isDark) "Light" else "Dark"),
        onClick = onToggleTheme,
        variant = AppButtonVariant.Secondary,
        modifier = Modifier.padding(horizontal = AppTheme.spacing.lg),
    )
}

@Composable
private fun ColorsSection(isDark: Boolean) {
    val colors = AppTheme.colors
    SectionHeader(if (isDark) "Dark palette" else "Light palette")
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
    ) {
        colors.roles().forEach { (name, color) -> Swatch(name = name, color = color) }
    }
}

/** Listed by hand: reflecting a `data class` is not free on native or wasm. */
private fun AppColors.roles(): List<Pair<String, Color>> = listOf(
    "background" to background,
    "surface" to surface,
    "surfaceRaised" to surfaceRaised,
    "primary" to primary,
    "onPrimary" to onPrimary,
    "accent" to accent,
    "textPrimary" to textPrimary,
    "textSecondary" to textSecondary,
    "textDisabled" to textDisabled,
    "outline" to outline,
    "success" to success,
    "warning" to warning,
    "error" to error,
    "onError" to onError,
)

@Composable
private fun Swatch(name: String, color: Color) = Column(
    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    modifier = Modifier.width(SWATCH_LABEL_WIDTH),
) {
    Box(
        modifier = Modifier
            .size(SWATCH_SIZE)
            .clip(AppTheme.shapes.small)
            .background(color),
    )
    AppText(StringValue.Raw(name), style = AppTheme.typography.label)
}

/** Resize the window and `windowSize` changes here, which is the fastest way to see a breakpoint. */
@Composable
private fun MetricValue(name: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(StringValue.Raw(name), style = AppTheme.typography.label, modifier = Modifier.width(METRIC_LABEL_WIDTH))
        AppText(StringValue.Raw(value), style = AppTheme.typography.body)
    }
}

@Composable
private fun MetricsSection() {
    val spacing = AppTheme.spacing
    val sizing = AppTheme.sizing

    SectionHeader("Spacing")
    listOf(
        "xxs" to spacing.xxs,
        "xs" to spacing.xs,
        "sm" to spacing.sm,
        "md" to spacing.md,
        "lg" to spacing.lg,
        "xl" to spacing.xl,
        "xxl" to spacing.xxl,
    ).forEach { (name, value) -> MetricBar(name, value) }

    SectionHeader("Sizing")
    listOf(
        "iconInline" to sizing.iconInline,
        "iconSmall" to sizing.iconSmall,
        "icon" to sizing.icon,
        "iconLarge" to sizing.iconLarge,
        "iconDisplay" to sizing.iconDisplay,
        "border" to sizing.border,
        "borderFocused" to sizing.borderFocused,
    ).forEach { (name, value) -> MetricBar(name, value) }

    SectionHeader("Layout")
    listOf(
        "windowSize" to AppTheme.windowSize.name,
        "mediumFrom" to AppWindowSize.mediumFrom.toString(),
        "expandedFrom" to AppWindowSize.expandedFrom.toString(),
        "contentMaxWidth" to sizing.contentMaxWidth.toString(),
        "listPaneWidth" to sizing.listPaneWidth.toString(),
    ).forEach { (name, value) -> MetricValue(name, value) }

    SectionHeader("Shapes")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg)) {
        listOf(
            "small" to AppTheme.shapes.small,
            "medium" to AppTheme.shapes.medium,
            "large" to AppTheme.shapes.large,
            "pill" to AppTheme.shapes.pill,
        ).forEach { (name, shape) ->
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)) {
                Box(
                    modifier = Modifier
                        .size(SWATCH_SIZE)
                        .clip(shape)
                        .background(AppTheme.colors.primary),
                )
                AppText(StringValue.Raw(name), style = AppTheme.typography.label)
            }
        }
    }
}

@Composable
private fun MetricBar(name: String, value: Dp) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
) {
    AppText(
        StringValue.Raw(name),
        style = AppTheme.typography.label,
        modifier = Modifier.width(METRIC_LABEL_WIDTH),
    )
    Box(
        modifier = Modifier
            .width(value)
            .height(METRIC_BAR_HEIGHT)
            .background(AppTheme.colors.accent),
    )
    AppText(StringValue.Raw("$value"), style = AppTheme.typography.bodySmall)
}

@Composable
private fun ComponentsSection() {
    SectionHeader("AppText")
    AppTextShowcase()
    SectionHeader("AppButton")
    AppButtonShowcase()
    SectionHeader("AppCard")
    AppCardShowcase()
    SectionHeader("AppTextField")
    AppTextFieldShowcase()
    SectionHeader("AppTopBar")
    AppTopBarShowcase()
    SectionHeader("AppImage")
    AppImageShowcase()
    SectionHeader("Feedback states")
    StateViewsShowcase()
}

@Composable
private fun SectionHeader(title: String) = Column(
    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    modifier = Modifier.padding(top = AppTheme.spacing.lg),
) {
    AppText(StringValue.Raw(title), style = AppTheme.typography.title)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.sizing.border)
            .background(AppTheme.colors.outline),
    )
}
