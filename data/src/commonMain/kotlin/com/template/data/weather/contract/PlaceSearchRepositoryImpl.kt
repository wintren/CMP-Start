package com.template.data.weather.contract

import com.template.data.weather.mapper.toGeoLocation
import com.template.data.weather.source.remote.OpenMeteoGeocodingSource
import com.template.domain.weather.contract.PlaceSearchRepository
import com.template.domain.weather.model.GeoLocation

internal class PlaceSearchRepositoryImpl(
    private val geocodingSource: OpenMeteoGeocodingSource,
) : PlaceSearchRepository {

    override suspend fun search(query: String): List<GeoLocation> {
        if (query.length < MINIMUM_QUERY_LENGTH) return emptyList()
        return geocodingSource.search(query).results.orEmpty().map { it.toGeoLocation() }
    }

    private companion object {
        const val MINIMUM_QUERY_LENGTH = 2
    }
}
