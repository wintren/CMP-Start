package com.template.core.common.logging

import android.util.Log as AndroidLog

/** Logcat renders level and tag itself, so the line carries only the call site and message. */
internal actual fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    val write: (String, String, Throwable?) -> Int = when (level) {
        LogLevel.Verbose -> AndroidLog::v
        LogLevel.Debug -> AndroidLog::d
        LogLevel.Info -> AndroidLog::i
        LogLevel.Warn -> AndroidLog::w
        LogLevel.Error -> AndroidLog::e
    }
    write(tag, message, throwable)
}
