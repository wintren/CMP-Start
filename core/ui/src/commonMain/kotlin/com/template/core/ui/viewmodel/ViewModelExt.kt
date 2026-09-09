package com.template.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.common.logging.Log
import com.template.core.ui.error.asMessage
import com.template.core.ui.resource.StringValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Named `fire` so a call site reads as "do this", not "start a coroutine".
 *
 * The `try` is the point: "throw in `:domain` and `:data`, catch in the ViewModel" is a rule the
 * ViewModel used to have to remember, and one forgotten `runCatching` took the process down from a
 * background refresh. A failure here reaches [onError] as something a screen can show, and is
 * always logged at error level whether or not anyone is listening — `Log.onLog` is where a crash
 * reporter picks it up.
 *
 * @param onError given the message for the failure. Omit it only when the work is genuinely
 * best-effort and the screen has nowhere to put the news.
 */
fun ViewModel.fire(
    onError: ((StringValue) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job = viewModelScope.launch {
    try {
        block()
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (failure: Throwable) {
        Log.e(this@fire::class.simpleName, failure) { "Failed: ${failure.message}" }
        onError?.invoke(failure.asMessage())
    }
}
