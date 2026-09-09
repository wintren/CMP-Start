package com.template.core.ui.error

import com.template.core.common.error.AppException
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
import com.template.core.ui.resources.Res
import com.template.core.ui.resources.error_offline
import com.template.core.ui.resources.error_server
import com.template.core.ui.resources.error_timeout
import com.template.core.ui.resources.error_unexpected

/**
 * The generic answer, so every screen is not obliged to invent one. A screen with something better
 * to say — "could not refresh *this* forecast" — still says it; this is the floor, not the ceiling.
 */
fun Throwable.asMessage(): StringValue = when (this) {
    is AppException.Offline -> Res.string.error_offline.asValue()
    is AppException.Timeout -> Res.string.error_timeout.asValue()
    is AppException.Server -> Res.string.error_server.asValue()
    else -> Res.string.error_unexpected.asValue()
}
