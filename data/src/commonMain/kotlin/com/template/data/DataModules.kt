package com.template.data

import com.template.data.network.HttpClientFactory
import com.template.data.weather.weatherDataModule
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private val infrastructureModule = module {
    single { HttpClientFactory.create(logRequests = true) }
    single { Json { ignoreUnknownKeys = true; encodeDefaults = true } }
}

val dataModules = listOf(infrastructureModule, platformDataModule, weatherDataModule)
