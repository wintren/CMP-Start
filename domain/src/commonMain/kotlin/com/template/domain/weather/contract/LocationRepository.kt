package com.template.domain.weather.contract

import com.template.domain.weather.model.GeoLocation
import kotlinx.coroutines.flow.Flow

/**
 * The locations the user has saved. Dumb verbs only — a repository stores facts, it doesn't decide
 * anything. Implemented in `:data`, in the package mirroring this one.
 */
interface LocationRepository {
    fun observeSaved(): Flow<List<GeoLocation>>
    suspend fun getSaved(): List<GeoLocation>
    suspend fun save(location: GeoLocation)
    suspend fun remove(locationId: Long)
}
