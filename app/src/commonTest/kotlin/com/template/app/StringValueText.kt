package com.template.app

import com.template.core.ui.resource.StringValue

/**
 * Reads the text out of a [StringValue] for assertions, without composition.
 *
 * `Res`-backed values are compared by resource, not by their English text — which is the point of
 * keeping `StringValue` in state rather than a resolved `String`.
 */
fun StringValue.text(): String = when (this) {
    StringValue.Empty -> ""
    is StringValue.Raw -> value
    is StringValue.Res -> resource.key
    is StringValue.Transformed -> transform(input.text())
}
