package com.template.core.ui.viewmodel

import androidx.lifecycle.viewModelScope

/**
 * [StateViewModel] plus one-shot events. Most screens don't need this: navigation goes through the
 * injected navigator, and anything the user can still see belongs in state. Use it for effects
 * that genuinely happen once — a snackbar, a share sheet, a clipboard write.
 */
abstract class StateEventViewModel<S, E> : StateViewModel<S>(), WithEvents<E> {

    override val eventsDelegate: EventDelegate<E> = EventDelegate(viewModelScope)

    protected fun sendEvent(event: E) = eventsDelegate.send(event)
}
