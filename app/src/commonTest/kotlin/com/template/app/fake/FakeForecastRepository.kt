package com.template.app.fake

import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.model.Forecast
import com.template.domain.weather.model.GeoLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeForecastRepository(
    initial: Map<Long, Forecast> = emptyMap(),
) : ForecastRepository {

    val cache = MutableStateFlow(initial)

    /** Set to make `refresh` throw, so the error path can be tested without a network. */
    var refreshFailure: Throwable? = null

    var refreshCount: Int = 0
        private set

    override fun observe(locationId: Long): Flow<Forecast?> = cache.map { it[locationId] }

    override fun observeAll(): Flow<Map<Long, Forecast>> = cache

    override suspend fun refresh(location: GeoLocation) {
        refreshCount++
        refreshFailure?.let { throw it }
    }

    override suspend fun refreshAll(locations: List<GeoLocation>) {
        locations.forEach { refresh(it) }
    }

    fun emit(forecast: Forecast) {
        cache.update { it + (forecast.locationId to forecast) }
    }
}
