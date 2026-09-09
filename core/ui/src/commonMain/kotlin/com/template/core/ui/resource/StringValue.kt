package com.template.core.ui.resource

import org.jetbrains.compose.resources.StringResource

/**
 * A string a screen will show, named but not yet resolved, so a ViewModel stays free of
 * Compose and a state assertion compares a resource rather than an English sentence.
 */
sealed interface StringValue {

    data object Empty : StringValue

    /** Already-final text: a place name, a user's input, a number formatted upstream. */
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
