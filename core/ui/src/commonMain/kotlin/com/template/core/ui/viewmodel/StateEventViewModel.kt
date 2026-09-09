package com.template.core.ui.viewmodel

import androidx.lifecycle.viewModelScope

/**
 * For effects that genuinely happen once — a snackbar, a share sheet, a clipboard write.
 * Anything the user can still see belongs in state instead.
 */
abstract class StateEventViewModel<S, E> : StateViewModel<S>(), WithEvents<E> {

    override val eventsDelegate: EventDelegate<E> = EventDelegate(viewModelScope)

    protected fun sendEvent(event: E) = eventsDelegate.send(event)
}
