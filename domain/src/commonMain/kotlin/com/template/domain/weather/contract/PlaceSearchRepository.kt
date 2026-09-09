package com.template.domain.weather.contract

import com.template.domain.weather.model.GeoLocation

interface PlaceSearchRepository {
    suspend fun search(query: String): List<GeoLocation>
}
