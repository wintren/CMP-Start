package com.template.core.common.logging

object Log {

    var minimumLevel: LogLevel = LogLevel.Verbose

    val isDebug: Boolean get() = minimumLevel <= LogLevel.Debug

    /** Second destination for anything that passes the filter — crash reporter, in-app viewer. */
    var onLog: ((level: LogLevel, tag: String, message: String, throwable: Throwable?) -> Unit)? = null

    fun v(tag: String? = null, message: () -> String) =
        log(LogLevel.Verbose, tag, null, message)

    fun d(tag: String? = null, message: () -> String) =
        log(LogLevel.Debug, tag, null, message)

    fun i(tag: String? = null, message: () -> String) =
        log(LogLevel.Info, tag, null, message)

    fun w(tag: String? = null, throwable: Throwable? = null, message: () -> String) =
        log(LogLevel.Warn, tag, throwable, message)

    fun e(tag: String? = null, throwable: Throwable? = null, message: () -> String) =
        log(LogLevel.Error, tag, throwable, message)

    private fun log(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: () -> String,
    ) {
        if (level < minimumLevel) return

        // Both the stack walk and the caller's string stay below the filter.
        val site = callSite()
        val text = if (site == null) message() else "$site ${message()}"

        platformLog(level, tag ?: DEFAULT_TAG, text, throwable)
        onLog?.invoke(level, tag ?: DEFAULT_TAG, text, throwable)
    }

    const val DEFAULT_TAG = "App"
}

/** Logs `label: format(this)` and returns the receiver, for inspecting a value mid-expression. */
inline fun <T> T.log(
    label: String,
    tag: String? = null,
    crossinline format: (T) -> String = { it.toString() },
): T = also { Log.d(tag) { "$label: ${format(it)}" } }
