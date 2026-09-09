package com.template.core.common.logging

/** The browser console renders no ANSI, so the level is an emoji. */
internal actual fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    println("${level.emoji} $tag $message")
    throwable?.printStackTrace()
}
