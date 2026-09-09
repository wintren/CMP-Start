package com.template.app

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import io.ktor.client.HttpClient

/**
 * Coil's default would build a second HTTP stack, and on iOS and wasmJs it has no fetcher
 * at all without this.
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
