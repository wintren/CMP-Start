package com.template.data.weather.source.local

import kotlinx.serialization.Serializable

/** Its own type, so a schema change is not a domain change. */
@Serializable
internal data class SavedLocationEntity(
    val id: Long,
    val name: String,
    val country: String? = null,
    val region: String? = null,
    val latitude: Double,
    val longitude: Double,
    val timeZone: String,
)
