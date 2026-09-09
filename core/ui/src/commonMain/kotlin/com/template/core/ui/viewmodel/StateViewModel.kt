package com.template.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.common.flow.StateFlowMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

/**
 * Base for every screen ViewModel.
 *
 * The contract that makes this worth having: **[stateFlow] is derived, never assigned.** Build it
 * once with [viewModelState] and the screen's state has a single, traceable source. Local UI state
 * (an expanded row, a selected tab, a text field) is a `private MutableStateFlow` fed into the
 * `data` block — an action updates that flow, and the state recomputes. Nothing in the ViewModel
 * ever pushes a value into `stateFlow` imperatively.
 *
 * The alternative — a `MutableStateFlow<State>` that a dozen methods each `update` — is what this
 * class exists to prevent. Once state can be written from anywhere, "why is the screen showing
 * this?" stops having an answer you can read.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class StateViewModel<S> : ViewModel(), WithState<S> {

    /** Rendered before the first `data` emission arrives. Must be cheap and synchronous. */
    abstract fun initialState(): S

    open val stateFlowMode: StateFlowMode = StateFlowMode.FetchEachStart

    /** The last published state. For actions that need to read what the user is looking at. */
    val state: S get() = stateFlow.value

    /**
     * @param data the reactive inputs and the expensive work. Merge several flows with
     *   [com.template.core.common.flow.combines].
     * @param state maps the result of [data] into display-ready values. Keep it light — it runs on
     *   every emission.
     * @param onStarted runs once when collection begins. The place for a refresh-on-open.
     */
    protected fun <T> viewModelState(
        data: () -> Flow<T>,
        state: suspend (T) -> S,
        onStarted: suspend () -> Unit = {},
    ): StateFlow<S> = viewModelState({ flowOf(Unit) }, { data() }, state, onStarted)

    /**
     * The overload with a [parameters] stage, for when several flows inside [data] depend on the
     * same upstream value.
     *
     * [parameters] resolves that value first and [data] is re-subscribed through `flatMapLatest`,
     * so one upstream change produces **one** emission rather than one per flow that observes it.
     * Reach for this when a screen keyed on an id combines several id-dependent sources.
     */
    protected fun <P, T> viewModelState(
        parameters: () -> Flow<P>,
        data: (P) -> Flow<T>,
        state: suspend (T) -> S,
        onStarted: suspend () -> Unit = {},
    ): StateFlow<S> = parameters()
        .flatMapLatest { data(it) }
        .mapLatest(state)
        .onStart { onStarted() }
        .stateIn(
            scope = viewModelScope,
            started = stateFlowMode(),
            initialValue = initialState(),
        )
}
