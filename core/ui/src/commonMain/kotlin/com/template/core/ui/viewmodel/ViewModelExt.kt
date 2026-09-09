package com.template.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Fire-and-forget work on `viewModelScope`, for actions that write. Named `fire` rather than
 * `launch` so a call site reads as "do this" instead of "start a coroutine".
 */
fun ViewModel.fire(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch(block = block)
