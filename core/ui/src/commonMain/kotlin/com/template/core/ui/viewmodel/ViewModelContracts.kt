package com.template.core.ui.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface WithState<S> {
    val stateFlow: StateFlow<S>
}

interface WithEvents<E> {
    val eventsDelegate: EventDelegate<E>
}

interface WithActions<A : Any> {
    fun onAction(action: A)
}
