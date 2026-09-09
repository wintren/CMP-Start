package com.template.core.common.error

/**
 * The failure vocabulary the UI is allowed to act on. `:data` maps every transport failure to one
 * of these, so a ViewModel can tell "you are offline" from "we are broken" without importing Ktor
 * — which the UI lane may not do, and should not want to.
 *
 * The messages are for logs and stack traces. What a person reads comes from
 * `Throwable.asMessage()` in `:core:ui`, which is where the strings live.
 */
sealed class AppException(message: String, cause: Throwable?) : Exception(message, cause) {

    /** Nothing reached the server: no route, DNS, TLS, refused, or an unclassifiable transport error. */
    class Offline(cause: Throwable? = null) : AppException("Offline", cause)

    class Timeout(cause: Throwable? = null) : AppException("Timed out", cause)

    /** [status] is null when the failure was not an HTTP status at all. */
    class Server(val status: Int?, cause: Throwable? = null) :
        AppException("Server error${status?.let { " $it" } ?: ""}", cause)

    /** A response arrived and did not match the model: a bug, ours or theirs, never the network's. */
    class Malformed(cause: Throwable? = null) : AppException("Malformed response", cause)
}
