@file:Suppress("ComposableNaming")

package com.template.core.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle

/**
 * The only two ways a screen touches its ViewModel:
 *
 * ```
 * val state by viewModel.collectState()
 * MyScreen(state = state, onAction = viewModel::onAction)
 * viewModel.collectEvents { event -> ... }
 * ```
 *
 * Both are lifecycle-aware, so a backgrounded screen stops recomposing and stops consuming events.
 */
@Composable
fun <S> WithState<S>.collectState(): State<S> = stateFlow.collectAsStateWithLifecycle()

@Composable
fun <E> WithEvents<E>.collectEvents(onEvent: (event: E) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        eventsDelegate.eventsFlow.flowWithLifecycle(lifecycle).collect(onEvent)
    }
}
