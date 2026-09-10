package com.template.core.common.logging

import platform.Foundation.NSLog

/**
 * One argument, with `%` doubled, rather than `NSLog("%@", line)`: Objective-C variadics reach
 * Kotlin/Native as `vararg Any?` and passing a Kotlin `String` for a `%@` specifier segfaults the
 * simulator. Escaping instead keeps a stray `%` in a message from being read as a specifier.
 *
 * `os_log` is a C macro, so it needs a cinterop wrapper and a symbol the iOS host supplies at
 * link time.
 */
internal actual fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    NSLog("[${level.token}] $tag ${message.replace("%", "%%")}")
    throwable?.printStackTrace()
}
