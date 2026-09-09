package com.template.design.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.resolve
import com.template.design.preview.AppPreview
import com.template.design.theme.AppTheme

/**
 * The only `Text` a screen should call. Takes a [StringValue] so a ViewModel can put an
 * unresolved, translatable string in its state and the leaf resolves it.
 */
@Composable
fun AppText(
    text: StringValue,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = AppTheme.colors.textPrimary,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textAlign: TextAlign? = null,
) = Text(
    text = text.resolve(),
    modifier = modifier,
    style = style,
    color = color,
    maxLines = maxLines,
    overflow = overflow,
    textAlign = textAlign,
)

/** The whole type scale, in the order it steps down. */
@Composable
fun AppTextShowcase() {
    val typography = AppTheme.typography
    listOf(
        "display" to typography.display,
        "title" to typography.title,
        "heading" to typography.heading,
        "subtitle" to typography.subtitle,
        "body" to typography.body,
        "bodySmall" to typography.bodySmall,
        "label" to typography.label,
        "numeric" to typography.numeric,
    ).forEach { (name, style) ->
        AppText(StringValue.Raw("$name — 21\u00B0C"), style = style)
    }
}

@Preview
@Composable
private fun AppTextLightPreview() = AppPreview { AppTextShowcase() }

@Preview
@Composable
private fun AppTextDarkPreview() = AppPreview(isDark = true) { AppTextShowcase() }
