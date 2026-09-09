package com.template.data.network

import com.template.core.common.error.AppException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.ContentConvertException
import kotlinx.serialization.SerializationException

/**
 * Everything the HTTP pipeline can throw, reduced to the four cases a screen can say something
 * useful about. Installed once in [HttpClientFactory], so no Source has to remember to do it.
 *
 * The `else` branch is [AppException.Offline] on purpose: an unclassified failure out of a network
 * call is a network problem far more often than anything else, and "check your connection" is the
 * one piece of advice that helps when we are guessing.
 */
internal fun Throwable.asAppException(): AppException = when (this) {
    is AppException -> this
    is ResponseException -> AppException.Server(response.status.value, this)
    is ContentConvertException, is SerializationException -> AppException.Malformed(this)
    is HttpRequestTimeoutException,
    is ConnectTimeoutException,
    is SocketTimeoutException,
    -> AppException.Timeout(this)

    else -> AppException.Offline(this)
}

/** Worth another attempt: the same request may well succeed in a moment. */
internal fun Throwable.isTransient(): Boolean = when (asAppException()) {
    is AppException.Offline, is AppException.Timeout -> true
    is AppException.Server, is AppException.Malformed -> false
}
