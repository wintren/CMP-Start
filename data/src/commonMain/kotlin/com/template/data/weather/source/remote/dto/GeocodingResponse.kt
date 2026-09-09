package com.template.data.weather.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GeocodingResponse(
    // Absent, not empty, when nothing matches.
    val results: List<PlaceDto>? = null,
)

@Serializable
internal data class PlaceDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    @SerialName("admin1") val region: String? = null,
    val timezone: String? = null,
)
