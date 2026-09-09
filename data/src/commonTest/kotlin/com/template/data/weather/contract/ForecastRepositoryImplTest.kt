package com.template.data.weather.contract

import com.template.data.weather.mapper.FORECAST_JSON
import com.template.data.weather.source.remote.OpenMeteoForecastSource
import com.template.domain.weather.model.GeoLocation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * The data path end to end, with the network faked at the engine — a `MockEngine`, not a mocked
 * repository, so the request, the JSON, the mapper and the cache are all really exercised.
 *
 * Note there is no mocking library anywhere in this project. Fakes and `MockEngine` cover it, and
 * they do not tie the build to a Kotlin-version-locked compiler plugin.
 */
class ForecastRepositoryImplTest {

    private val berlin = GeoLocation(
        id = 42L,
        name = "Berlin",
        country = "Germany",
        region = "Berlin",
        latitude = 52.52,
        longitude = 13.41,
        timeZone = "Europe/Berlin",
    )

    private fun repository(engine: MockEngine) = ForecastRepositoryImpl(
        OpenMeteoForecastSource(
            HttpClient(engine) {
                expectSuccess = true
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; explicitNulls = false }) }
            }
        )
    )

    private fun okEngine() = MockEngine { _ ->
        respond(
            content = FORECAST_JSON,
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
        )
    }

    @Test
    fun `the cache is empty until something refreshes it`() = runTest {
        val repository = repository(okEngine())

        assertNull(repository.observe(berlin.id).first())
        assertTrue(repository.observeAll().first().isEmpty())
    }

    @Test
    fun `refresh fills the cache and observers see it`() = runTest {
        val repository = repository(okEngine())

        repository.refresh(berlin)

        val forecast = repository.observe(berlin.id).first()
        assertEquals(berlin.id, forecast?.locationId)
        assertEquals(7, forecast?.days?.size)
    }

    @Test
    fun `the request asks for the domain's canonical units`() = runTest {
        val engine = okEngine()
        repository(engine).refresh(berlin)

        val url = engine.requestHistory.single().url.toString()
        // If these ever change, every score in the app silently shifts.
        assertTrue("wind_speed_unit=ms" in url, url)
        assertTrue("temperature_unit=celsius" in url, url)
        assertTrue("precipitation_unit=mm" in url, url)
        assertTrue("latitude=52.52" in url, url)
    }

    @Test
    fun `a failed refresh throws instead of caching a hole`() = runTest {
        val repository = repository(MockEngine { respondError(HttpStatusCode.ServiceUnavailable) })

        val error = runCatching { repository.refresh(berlin) }.exceptionOrNull()

        assertTrue(error != null, "the repository must not swallow the failure")
        assertNull(repository.observe(berlin.id).first(), "nothing should be cached")
    }

    @Test
    fun `refreshing several locations caches each of them`() = runTest {
        val repository = repository(okEngine())
        val oslo = berlin.copy(id = 7L, name = "Oslo")

        repository.refreshAll(listOf(berlin, oslo))

        assertEquals(setOf(42L, 7L), repository.observeAll().first().keys)
    }
}
