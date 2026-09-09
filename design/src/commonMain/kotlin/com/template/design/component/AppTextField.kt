package com.template.design.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.resolve
import com.template.design.theme.AppTheme

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
                Icon(Icons.Default.Close, contentDescription = "Clear")
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
