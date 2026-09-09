package com.template.app.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform

/**
 * Starts the graph, once. Every `:launch:<platform>` module calls this, so the module list has one
 * home and a new module is added in one place.
 *
 * `KoinPlatform` rather than `GlobalContext`: the latter is not on the native source set.
 * The guard matters because a desktop hot-reload re-enters `main`, an iOS view controller can be
 * created more than once, and a wasm page can re-run its entry point.
 */
fun startAppKoin(configure: KoinApplication.() -> Unit = {}) {
    if (runCatching { KoinPlatform.getKoin() }.isSuccess) return
    startKoin {
        configure()
        modules(appModules)
    }
}
