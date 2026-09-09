package com.template.data

import com.template.data.network.HttpClientFactory
import com.template.data.weather.weatherDataModule
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private val infrastructureModule = module {
    // One serializer for the whole process. `explicitNulls = false`: an absent field and an
    // explicit null mean the same thing to these models.
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
        }
    }
    single { HttpClientFactory.create(json = get()) }
}

val dataModules = listOf(infrastructureModule, platformDataModule, weatherDataModule)
