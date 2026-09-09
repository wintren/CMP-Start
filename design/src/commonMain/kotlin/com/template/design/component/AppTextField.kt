package com.template.design.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.resolve
import com.template.design.resources.Res
import com.template.design.resources.action_clear
import com.template.design.preview.AppPreview
import com.template.design.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: StringValue,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onSubmit: (() -> Unit)? = null,
) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    singleLine = true,
    shape = AppTheme.shapes.medium,
    placeholder = { AppText(placeholder, color = AppTheme.colors.textDisabled) },
    trailingIcon = {
        if (value.isNotEmpty()) {
            IconButton(onClick = { onValueChange("") }) {
                Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.action_clear))
            }
        }
    },
    keyboardOptions = KeyboardOptions(
        imeAction = if (onSubmit != null) ImeAction.Search else ImeAction.Default,
    ),
    keyboardActions = KeyboardActions(onSearch = { onSubmit?.invoke() }),
    colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = AppTheme.colors.textPrimary,
        unfocusedTextColor = AppTheme.colors.textPrimary,
        focusedBorderColor = AppTheme.colors.primary,
        unfocusedBorderColor = AppTheme.colors.outline,
        cursorColor = AppTheme.colors.primary,
    ),
)

/** Empty (placeholder showing) and typed-in (clear button showing) are the two states. */
@Composable
fun AppTextFieldShowcase() {
    var empty by remember { mutableStateOf("") }
    var filled by remember { mutableStateOf("Gothenburg") }

    AppTextField(
        value = empty,
        onValueChange = { empty = it },
        placeholder = StringValue.Raw("Search for a city"),
    )
    AppTextField(
        value = filled,
        onValueChange = { filled = it },
        placeholder = StringValue.Raw("Search for a city"),
        onSubmit = {},
    )
}

@Preview
@Composable
private fun AppTextFieldLightPreview() = AppPreview { AppTextFieldShowcase() }

@Preview
@Composable
private fun AppTextFieldDarkPreview() = AppPreview(isDark = true) { AppTextFieldShowcase() }
