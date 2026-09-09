package com.template.data.network

import com.template.core.common.logging.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * No engine is named: each target's build file puts exactly one on its classpath and Ktor
 * resolves it, which is why this needs no `expect`/`actual`.
 */
object HttpClientFactory {

    /**
     * @param logRequests logs every URL and header. Defaults to [Log.isDebug]; `commonMain` has
     * no build-config flag to read, and `true` here would ship the request log.
     */
    fun create(json: Json, logRequests: Boolean = Log.isDebug): HttpClient = HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MS
            connectTimeoutMillis = CONNECT_TIMEOUT_MS
        }

        if (logRequests) {
            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) = Log.d(TAG) { message }
                }
            }
        }
    }

    private const val TAG = "Http"
    private const val REQUEST_TIMEOUT_MS = 20_000L
    private const val CONNECT_TIMEOUT_MS = 10_000L
}
