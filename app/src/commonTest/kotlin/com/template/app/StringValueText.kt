package com.template.app

import com.template.core.ui.resource.StringValue

/** `Res`-backed values compare by resource, not by their English text. */
fun StringValue.text(): String = when (this) {
    StringValue.Empty -> ""
    is StringValue.Raw -> value
    is StringValue.Res -> resource.key
    is StringValue.Transformed -> transform(input.text())
}
