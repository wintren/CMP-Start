package com.template.core.common.logging

/** The caller's call site as `(BestDayViewModel.kt:88)`, or null where it is too costly to find. */
internal expect fun callSite(): String?
