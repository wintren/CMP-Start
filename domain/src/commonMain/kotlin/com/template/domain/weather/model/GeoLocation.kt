package com.template.domain.weather.model

data class GeoLocation(
    val id: Long,
    val name: String,
    val country: String?,
    val region: String?,
    val latitude: Double,
    val longitude: Double,
    val timeZone: String,
)
