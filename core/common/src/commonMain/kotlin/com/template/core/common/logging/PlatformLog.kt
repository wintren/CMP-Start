package com.template.core.common.logging

internal expect fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
)
