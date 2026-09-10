package com.template.data.network

import com.template.core.common.config.AppConfig
import com.template.core.common.logging.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * No engine is named: each target's build file puts exactly one on its classpath and Ktor
 * resolves it, which is why this needs no `expect`/`actual`.
 */
object HttpClientFactory {

    /** @param logRequests logs every URL and header. Off outside dev — see config/prod.properties. */
    fun create(json: Json, logRequests: Boolean = AppConfig.logRequests): HttpClient = HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        // Empty on the free Open-Meteo tier. Here as the worked example of a secret that arrives
        // from local.properties or APP_API_TOKEN rather than from the repository.
        if (AppConfig.apiToken.isNotBlank()) {
            defaultRequest {
                header(HttpHeaders.Authorization, "Bearer ${AppConfig.apiToken}")
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MS
            connectTimeoutMillis = CONNECT_TIMEOUT_MS
        }

        /**
         * A phone loses its connection mid-request often enough that one retry is the difference
         * between an error screen and nothing happening at all. `exponentialDelay` starts at one
         * second, so the last attempt lands well inside the request timeout above.
         */
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = MAX_RETRIES)
            retryOnExceptionIf(maxRetries = MAX_RETRIES) { _, cause -> cause.isTransient() }
            exponentialDelay()
        }

        if (logRequests) {
            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) = Log.d(TAG) { message }
                }
            }
        }

        // The last thing the pipeline does, so a Source never sees a Ktor type and the UI lane
        // never has to know one. Retries above have already been spent by the time this runs.
        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ -> throw cause.asAppException() }
        }
    }

    private const val TAG = "Http"
    private const val REQUEST_TIMEOUT_MS = 20_000L
    private const val CONNECT_TIMEOUT_MS = 10_000L
    private const val MAX_RETRIES = 2
}
