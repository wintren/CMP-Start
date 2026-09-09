package com.template.data.weather.contract

import com.template.data.weather.mapper.toForecast
import com.template.data.weather.source.remote.OpenMeteoForecastSource
import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.model.Forecast
import com.template.domain.weather.model.GeoLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * The cache is not persisted, so a cold start shows a spinner rather than yesterday's
 * forecast. Persist it behind another Source if you need offline.
 */
internal class ForecastRepositoryImpl(
    private val forecastSource: OpenMeteoForecastSource,
) : ForecastRepository {

    private val cache = MutableStateFlow<Map<Long, Forecast>>(emptyMap())

    override fun observe(locationId: Long): Flow<Forecast?> =
        cache.map { it[locationId] }.distinctUntilChanged()

    override fun observeAll(): Flow<Map<Long, Forecast>> = cache.asStateFlow()

    override suspend fun refresh(location: GeoLocation) {
        val forecast = forecastSource
            .fetch(latitude = location.latitude, longitude = location.longitude)
            .toForecast(location.id)
        cache.update { it + (location.id to forecast) }
    }

    override suspend fun refreshAll(locations: List<GeoLocation>) {
        locations.forEach { refresh(it) }
    }
}
