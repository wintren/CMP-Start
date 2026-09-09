package com.template.core.common.logging

/** Declaration order is the severity order — [Log.minimumLevel] compares with `<`. */
enum class LogLevel(val token: Char, val emoji: String, private val ansiColor: Int) {
    Verbose('V', "⚪", 37),
    Debug('D', "🔵", 36),
    Info('I', "🟢", 92),
    Warn('W', "⚠️", 93),
    Error('E', "⛔", 91);

    /** Only the token is wrapped; an escape sequence around the line stops IDEs linking it. */
    fun colorToken(): String = "$ESCAPE[${ansiColor}m[$token]$ESCAPE[0m"

    private companion object {
        const val ESCAPE = "\u001B"
    }
}
