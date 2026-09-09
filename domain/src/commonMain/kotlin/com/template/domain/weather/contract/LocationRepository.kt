package com.template.domain.weather.contract

import com.template.domain.weather.model.GeoLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun observeSaved(): Flow<List<GeoLocation>>
    suspend fun getSaved(): List<GeoLocation>
    suspend fun save(location: GeoLocation)
    suspend fun remove(locationId: Long)
}
