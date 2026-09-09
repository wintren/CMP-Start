package com.template.design.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.resolve
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
