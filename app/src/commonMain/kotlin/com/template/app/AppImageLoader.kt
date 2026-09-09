package com.template.app

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import io.ktor.client.HttpClient

/**
 * Coil's singleton loader, given the app's own Ktor client.
 *
 * The client is injected rather than left to Coil so there is exactly one HTTP stack in the
 * process — one engine, one connection pool, one place logging is turned on. Coil's default
 * would build a second one, and on iOS and wasmJs it has no fetcher at all without this.
 */
@Composable
internal fun installAppImageLoader(httpClient: HttpClient) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory(httpClient = { httpClient })) }
            .crossfade(true)
            .build()
    }
}
