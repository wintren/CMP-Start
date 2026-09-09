package com.template.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** Named `fire` so a call site reads as "do this", not "start a coroutine". */
fun ViewModel.fire(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch(block = block)
