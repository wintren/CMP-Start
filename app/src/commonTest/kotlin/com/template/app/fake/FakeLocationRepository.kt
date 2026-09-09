package com.template.app.fake

import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.model.GeoLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Hand-written fakes, not mocks.
 *
 * A fake is a real implementation with a simple backing store, so a test reads as "given these
 * saved places" instead of a stack of `every { ... } returns ...`. It also survives refactors: add
 * a method to the interface and the compiler tells you, rather than a mock silently returning null.
 */
class FakeLocationRepository(
    initial: List<GeoLocation> = emptyList(),
) : LocationRepository {

    val saved = MutableStateFlow(initial)

    override fun observeSaved(): Flow<List<GeoLocation>> = saved

    override suspend fun getSaved(): List<GeoLocation> = saved.value

    override suspend fun save(location: GeoLocation) {
        saved.update { current -> current.filterNot { it.id == location.id } + location }
    }

    override suspend fun remove(locationId: Long) {
        saved.update { current -> current.filterNot { it.id == locationId } }
    }
}
