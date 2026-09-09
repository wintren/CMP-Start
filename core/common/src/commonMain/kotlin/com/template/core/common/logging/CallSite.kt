package com.template.core.common.logging

/**
 * `(BestDayViewModel.kt:88)` for the caller, or null where a stack walk costs too much: Native has
 * to symbolicate, and wasm frames name compiled output. Pass a `tag` for those targets.
 */
internal expect fun callSite(): String?
