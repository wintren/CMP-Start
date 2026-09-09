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
 * The one place an [HttpClient] is built.
 *
 * No engine is named: each target's build file puts exactly one engine on its classpath (OkHttp,
 * Darwin, CIO, JS) and Ktor resolves it. That is why this file needs no `expect`/`actual`.
 */
object HttpClientFactory {

    fun create(logRequests: Boolean = false): HttpClient = HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    // The wire adds fields without warning; a new one must not break parsing.
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MS
            connectTimeoutMillis = CONNECT_TIMEOUT_MS
        }

        if (logRequests) {
            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) = Log.d("Http") { message }
                }
            }
        }
    }

    private const val REQUEST_TIMEOUT_MS = 20_000L
    private const val CONNECT_TIMEOUT_MS = 10_000L
}
