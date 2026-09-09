package com.template.core.common.logging

/**
 * Frames are skipped by *file*: filtering by package would skip a caller in this package too.
 * R8 needs `-keepattributes SourceFile,LineNumberTable` or this reads `(SourceFile:1)`.
 */
internal actual fun callSite(): String? = Throwable().stackTrace
    .firstOrNull { it.fileName !in LOGGER_FILES }
    ?.let { "(${it.fileName}:${it.lineNumber})" }

private val LOGGER_FILES = setOf("Log.kt", "CallSite.android.kt")
