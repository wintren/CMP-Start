package com.template.core.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * One-shot events, as a rendezvous [Channel] rather than a `SharedFlow`, so nothing is dropped
 * while the screen is away and nothing is replayed when it comes back.
 *
 * A Channel does not support multiple *first* subscribers
 * (https://github.com/Kotlin/kotlinx.coroutines/issues/3002). That is fine here: one ViewModel
 * belongs to one screen. `receiveAsFlow` (not `consumeAsFlow`) is deliberate — the latter cancels
 * the channel on collection, which crashes the second collection after a configuration change.
 */
class EventDelegate<E>(private val scope: CoroutineScope) {

    private val events = Channel<E>()

    val eventsFlow: Flow<E> = events.receiveAsFlow()

    fun send(event: E) {
        scope.launch { events.send(event) }
    }
}
