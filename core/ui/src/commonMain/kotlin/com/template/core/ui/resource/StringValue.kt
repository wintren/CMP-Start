package com.template.core.ui.resource

import org.jetbrains.compose.resources.StringResource

/**
 * A string a screen will show, named but not yet resolved.
 *
 * ViewModels put `StringValue` in state instead of `String`, which keeps them free of Compose and
 * of the platform's resource lookup: a state assertion in a unit test compares
 * `StringValue.Res(Res.string.error_offline)`, not an English sentence that a translator will
 * change next week.
 *
 * Resolve at the leaf, with [com.template.core.ui.resource.resolve].
 */
sealed interface StringValue {

    data object Empty : StringValue

    /** Already-final text: a place name, a user's input, a number that was formatted upstream. */
    data class Raw(val value: String) : StringValue

    data class Res(
        val resource: StringResource,
        val args: List<StringValue> = emptyList(),
    ) : StringValue

    /** Wraps another value in a transform applied after resolution (case, truncation, padding). */
    data class Transformed(
        val input: StringValue,
        val transform: (String) -> String,
    ) : StringValue

    companion object {
        fun of(value: String): StringValue = Raw(value)
        fun of(value: Int): StringValue = Raw(value.toString())
    }
}

fun StringResource.asValue(vararg args: StringValue): StringValue.Res =
    StringValue.Res(this, args.toList())

fun String.asValue(): StringValue = StringValue.Raw(this)

fun StringValue.transformed(transform: (String) -> String): StringValue =
    StringValue.Transformed(this, transform)

fun StringValue.uppercase(): StringValue = transformed { it.uppercase() }
