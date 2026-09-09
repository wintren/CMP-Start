package com.template.core.common.logging

/**
 * Deliberately `println`: it reaches Logcat, stdout, the Xcode console and the browser console with
 * no expect/actual and no dependency.
 *
 * Swap the body for a per-platform logger when you need tags, levels at runtime, or crash
 * reporting — every call site here stays as it is.
 */
object Log {
    enum class Level { Verbose, Debug, Info, Warn, Error }

    var minimumLevel: Level = Level.Verbose

    fun v(tag: String, message: () -> String) = log(Level.Verbose, tag, null, message)
    fun d(tag: String, message: () -> String) = log(Level.Debug, tag, null, message)
    fun i(tag: String, message: () -> String) = log(Level.Info, tag, null, message)
    fun w(tag: String, message: () -> String) = log(Level.Warn, tag, null, message)
    fun e(tag: String, throwable: Throwable? = null, message: () -> String) =
        log(Level.Error, tag, throwable, message)

    private fun log(level: Level, tag: String, throwable: Throwable?, message: () -> String) {
        if (level < minimumLevel) return
        println("${level.name.first()}/$tag: ${message()}")
        throwable?.let { println("${level.name.first()}/$tag: ${it.stackTraceToString()}") }
    }
}
