package com.template.core.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * A rendezvous [Channel], not a `SharedFlow`, so nothing is dropped while the screen is away
 * and nothing is replayed when it returns.
 * `receiveAsFlow`, not `consumeAsFlow` — the latter cancels the channel on collection and
 * crashes the second collection after a configuration change. A Channel supports only one
 * first subscriber (Kotlin/kotlinx.coroutines#3002), which is fine for one ViewModel.
 */
class EventDelegate<E>(private val scope: CoroutineScope) {

    private val events = Channel<E>()

    val eventsFlow: Flow<E> = events.receiveAsFlow()

    fun send(event: E) {
        scope.launch { events.send(event) }
    }
}
