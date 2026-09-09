package com.template.core.ui.resource

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun StringValue.resolve(): String = when (this) {
    StringValue.Empty -> ""
    is StringValue.Raw -> value
    is StringValue.Res -> when {
        args.isEmpty() -> stringResource(resource)
        else -> stringResource(resource, *args.map { it.resolve() }.toTypedArray())
    }
    is StringValue.Transformed -> transform(input.resolve())
}

suspend fun StringValue.resolveAsString(): String = when (this) {
    StringValue.Empty -> ""
    is StringValue.Raw -> value
    is StringValue.Res -> when {
        args.isEmpty() -> getString(resource)
        else -> getString(resource, *args.map { it.resolveAsString() }.toTypedArray())
    }
    is StringValue.Transformed -> transform(input.resolveAsString())
}
