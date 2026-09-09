package com.template.data.weather.mapper

import com.template.data.weather.source.local.SavedLocationEntity
import com.template.data.weather.source.remote.dto.PlaceDto
import com.template.domain.weather.model.GeoLocation

private const val FALLBACK_TIME_ZONE = "UTC"

internal fun PlaceDto.toGeoLocation() = GeoLocation(
    id = id,
    name = name,
    country = country,
    region = region,
    latitude = latitude,
    longitude = longitude,
    timeZone = timezone ?: FALLBACK_TIME_ZONE,
)

internal fun SavedLocationEntity.toGeoLocation() = GeoLocation(
    id = id,
    name = name,
    country = country,
    region = region,
    latitude = latitude,
    longitude = longitude,
    timeZone = timeZone,
)

internal fun GeoLocation.toEntity() = SavedLocationEntity(
    id = id,
    name = name,
    country = country,
    region = region,
    latitude = latitude,
    longitude = longitude,
    timeZone = timeZone,
)
