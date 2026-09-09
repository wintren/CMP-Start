package com.template.core.common.logging

/** The call site stays outside the ANSI sequence; IntelliJ only links `(File.kt:88)` unwrapped. */
internal actual fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    println("${level.colorToken()} $tag $message")
    throwable?.printStackTrace()
}
