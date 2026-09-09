package com.template.app.di

import com.template.app.AppViewModel
import com.template.app.navigation.NavControls
import com.template.app.navigation.Navigator
import com.template.app.weather.bestday.BestDayViewModel
import com.template.app.weather.forecast.ForecastViewModel
import com.template.app.weather.locations.LocationsViewModel
import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.contract.PlaceSearchRepository
import com.template.domain.weather.logic.RankSavedLocationDays
import com.template.feature.settings.SettingsViewModel
import com.template.feature.settings.contract.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Builds the real graph and constructs everything in it.
 *
 * Koin resolves at runtime, so a missing binding is otherwise a crash on whichever screen needed it
 * — found by whoever opens that screen next, which on iOS or wasm can be much later. This test is
 * why the app cannot ship with a hole in its graph.
 *
 * It constructs rather than reflects, so it also runs each ViewModel's `init` and its
 * `viewModelState` builder — a `stateIn` that throws on construction fails here too.
 *
 * JVM-only, which is fine: the graph is declared in common code.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppModulesTest {

    // `viewModelScope` dispatches on Main, which does not exist in a plain JVM test.
    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private val koin = koinApplication { modules(appModules) }.koin

    @Test
    fun `the data layer resolves`() {
        assertNotNull(koin.get<LocationRepository>())
        assertNotNull(koin.get<ForecastRepository>())
        assertNotNull(koin.get<PlaceSearchRepository>())
        assertNotNull(koin.get<PreferencesRepository>())
    }

    @Test
    fun `the domain layer resolves`() {
        assertNotNull(koin.get<RankSavedLocationDays>())
    }

    @Test
    fun `navigation is one instance behind two types`() {
        val navigator = koin.get<Navigator>()
        val controls = koin.get<NavControls>()

        // A second Navigator would give ViewModels a back stack the UI is not rendering.
        kotlin.test.assertSame(navigator, controls)
    }

    @Test
    fun `every view model constructs`() {
        assertNotNull(koin.get<AppViewModel>())
        assertNotNull(koin.get<LocationsViewModel>())
        assertNotNull(koin.get<BestDayViewModel>())
        assertNotNull(koin.get<ForecastViewModel> { parametersOf(1L) })
        assertNotNull(koin.get<SettingsViewModel> { parametersOf({ }) })
    }
}
