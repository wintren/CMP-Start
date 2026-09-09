package com.template.core.common.logging

import platform.Foundation.NSLog

/**
 * `%@` with the line as an argument, never as the format string — a stray `%` in a message
 * would otherwise be read as a specifier. `os_log` is a C macro, so it needs a cinterop
 * wrapper and a symbol the iOS host supplies at link time.
 */
internal actual fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    NSLog("%@", "[${level.token}] $tag $message")
    throwable?.printStackTrace()
}
