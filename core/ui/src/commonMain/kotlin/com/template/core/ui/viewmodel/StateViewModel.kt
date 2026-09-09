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
 * A `MutableStateFlow<State>` that a dozen methods each `update` is what this prevents: once
 * state can be written from anywhere, "why is the screen showing this?" has no readable answer.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class StateViewModel<S> : ViewModel(), WithState<S> {

    /** Rendered before the first `data` emission arrives. Must be cheap and synchronous. */
    abstract fun initialState(): S

    open val stateFlowMode: StateFlowMode = StateFlowMode.FetchEachStart

    val state: S get() = stateFlow.value

    /**
     * @param data the reactive inputs and the expensive work.
     * @param state maps [data] into display-ready values; runs on every emission, so keep it light.
     * @param onStarted runs once when collection begins. The place for a refresh-on-open.
     */
    protected fun <T> viewModelState(
        data: () -> Flow<T>,
        state: suspend (T) -> S,
        onStarted: suspend () -> Unit = {},
    ): StateFlow<S> = viewModelState({ flowOf(Unit) }, { data() }, state, onStarted)

    /**
     * [parameters] resolves first and [data] is re-subscribed through `flatMapLatest`, so one
     * upstream change produces **one** emission rather than one per flow that observes it.
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
