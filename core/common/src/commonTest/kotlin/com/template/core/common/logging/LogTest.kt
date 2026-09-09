package com.template.core.common.logging

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LogTest {

    private val recorded = mutableListOf<Triple<LogLevel, String, String>>()

    private fun record() {
        Log.onLog = { level, tag, message, _ -> recorded += Triple(level, tag, message) }
    }

    @AfterTest
    fun reset() {
        Log.onLog = null
        Log.minimumLevel = LogLevel.Verbose
    }

    /** If this fails, every filtered-out call site has started paying for a discarded string. */
    @Test
    fun `a filtered level never invokes the message lambda`() {
        Log.minimumLevel = LogLevel.Warn
        var built = 0

        Log.v { "verbose".also { built++ } }
        Log.d { "debug".also { built++ } }
        Log.i { "info".also { built++ } }

        assertEquals(0, built, "The message lambda ran for a level that is filtered out")
    }

    @Test
    fun `a level at or above the minimum is logged`() {
        record()
        Log.minimumLevel = LogLevel.Warn

        Log.d(TAG) { "dropped" }
        Log.w(TAG) { "kept" }
        Log.e(TAG) { "kept too" }

        assertEquals(listOf(LogLevel.Warn, LogLevel.Error), recorded.map { it.first })
    }

    @Test
    fun `an explicit tag is used verbatim`() {
        record()

        Log.i(TAG) { "hello" }

        assertEquals(TAG, recorded.single().second)
    }

    @Test
    fun `an omitted tag falls back to the default`() {
        record()

        Log.i { "hello" }

        assertEquals(Log.DEFAULT_TAG, recorded.single().second)
    }

    @Test
    fun `log returns its receiver`() {
        record()

        val result = listOf(1, 2, 3).log("items", TAG) { it.size.toString() }

        assertEquals(listOf(1, 2, 3), result)
        assertTrue(recorded.single().third.endsWith("items: 3"))
    }

    private companion object {
        const val TAG = "LogTest"
    }
}
