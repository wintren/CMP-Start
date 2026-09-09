package com.template.core.common.flow

import kotlinx.coroutines.flow.SharingStarted

enum class StateFlowMode {
    /** Starts once and stays hot for the ViewModel's life. For setup work that must not repeat. */
    SingleStart,

    /** Re-runs the upstream on each new subscriber after a gap. The default for screen state. */
    FetchEachStart;

    operator fun invoke(): SharingStarted = when (this) {
        SingleStart -> SharingStarted.Lazily
        FetchEachStart -> SharingStarted.WhileSubscribed(TIME_BEFORE_RESTART)
    }

    companion object {
        /** 5s is Android's ANR threshold, and short enough that a rotation keeps the same state. */
        const val TIME_BEFORE_RESTART = 5_000L
    }
}
