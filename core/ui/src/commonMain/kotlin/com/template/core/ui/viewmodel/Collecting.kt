@file:Suppress("ComposableNaming")

package com.template.core.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle

/** Both are lifecycle-aware: a backgrounded screen stops recomposing and consuming events. */
@Composable
fun <S> WithState<S>.collectState(): State<S> = stateFlow.collectAsStateWithLifecycle()

@Composable
fun <E> WithEvents<E>.collectEvents(onEvent: (event: E) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        eventsDelegate.eventsFlow.flowWithLifecycle(lifecycle).collect(onEvent)
    }
}
