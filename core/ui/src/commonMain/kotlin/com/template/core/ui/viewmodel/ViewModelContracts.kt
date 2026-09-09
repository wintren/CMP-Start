package com.template.core.ui.viewmodel

import kotlinx.coroutines.flow.StateFlow

/** A ViewModel that publishes exactly one screen state. */
interface WithState<S> {
    val stateFlow: StateFlow<S>
}

/** A ViewModel that emits one-shot effects (toast, share sheet, focus) alongside its state. */
interface WithEvents<E> {
    val eventsDelegate: EventDelegate<E>
}

/** A ViewModel that receives everything the screen can do as one sealed type. */
interface WithActions<A : Any> {
    fun onAction(action: A)
}
