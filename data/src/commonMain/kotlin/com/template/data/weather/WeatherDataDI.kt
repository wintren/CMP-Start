package com.template.data.weather

import com.template.data.weather.contract.ForecastRepositoryImpl
import com.template.data.weather.contract.LocationRepositoryImpl
import com.template.data.weather.contract.PlaceSearchRepositoryImpl
import com.template.data.weather.source.remote.OpenMeteoForecastSource
import com.template.data.weather.source.remote.OpenMeteoGeocodingSource
import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.contract.PlaceSearchRepository
import org.koin.core.module.dsl.new
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * `SavedLocationStore` is bound in `platformDataModule` — its implementation depends on whether
 * the target has SQLite.
 *
 * `single<Interface> { new(::Impl) }` on purpose: only the interface is resolvable, so nothing can
 * accidentally depend on the implementation. Sources are bound concretely — they are internal to
 * this module and have no second implementation to swap.
 */
val weatherDataModule = module {
    singleOf(::OpenMeteoForecastSource)
    singleOf(::OpenMeteoGeocodingSource)

    single<ForecastRepository> { new(::ForecastRepositoryImpl) }
    single<LocationRepository> { new(::LocationRepositoryImpl) }
    single<PlaceSearchRepository> { new(::PlaceSearchRepositoryImpl) }
}
