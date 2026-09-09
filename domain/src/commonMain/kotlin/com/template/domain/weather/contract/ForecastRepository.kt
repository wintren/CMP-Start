package com.template.domain.weather.contract

import com.template.domain.weather.model.Forecast
import com.template.domain.weather.model.GeoLocation
import kotlinx.coroutines.flow.Flow

/**
 * Forecasts, cached per location. `observe*` reads the cache; `refresh*` fills it from the network
 * and throws on failure — errors travel up to the ViewModel, which is the layer that can show them.
 */
interface ForecastRepository {
    fun observe(locationId: Long): Flow<Forecast?>
    fun observeAll(): Flow<Map<Long, Forecast>>
    suspend fun refresh(location: GeoLocation)
    suspend fun refreshAll(locations: List<GeoLocation>)
}
