package com.template.data.weather.source.remote

import com.template.core.common.config.AppConfig
import com.template.data.weather.source.remote.dto.GeocodingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class OpenMeteoGeocodingSource(private val client: HttpClient) {

    suspend fun search(query: String): GeocodingResponse =
        client.get(GEOCODING_URL) {
            parameter("name", query)
            parameter("count", RESULT_LIMIT)
            parameter("language", "en")
            parameter("format", "json")
        }.body()

    private companion object {
        val GEOCODING_URL = "${AppConfig.geocodingBaseUrl}/search"
        const val RESULT_LIMIT = 10
    }
}
