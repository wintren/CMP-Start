package com.template.data.weather.contract

import com.template.data.weather.mapper.toEntity
import com.template.data.weather.mapper.toGeoLocation
import com.template.data.weather.source.local.SavedLocationStore
import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.model.GeoLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * `internal`, and placed in the package that mirrors the interface it implements — that pairing is
 * how you find an impl without a naming convention to remember.
 */
internal class LocationRepositoryImpl(
    private val savedLocationStore: SavedLocationStore,
) : LocationRepository {

    override fun observeSaved(): Flow<List<GeoLocation>> =
        savedLocationStore.observe().map { entities -> entities.map { it.toGeoLocation() } }

    override suspend fun getSaved(): List<GeoLocation> =
        savedLocationStore.get().map { it.toGeoLocation() }

    override suspend fun save(location: GeoLocation) =
        savedLocationStore.upsert(location.toEntity())

    override suspend fun remove(locationId: Long) = savedLocationStore.remove(locationId)
}
