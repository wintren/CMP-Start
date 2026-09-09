package com.template.app.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform

/**
 * The guard matters: a desktop hot-reload re-enters `main`, an iOS view controller can be
 * created more than once, and a wasm page can re-run its entry point.
 * `KoinPlatform` rather than `GlobalContext` — the latter is not on the native source set.
 */
fun startAppKoin(configure: KoinApplication.() -> Unit = {}) {
    if (runCatching { KoinPlatform.getKoin() }.isSuccess) return
    startKoin {
        configure()
        modules(appModules)
    }
}
