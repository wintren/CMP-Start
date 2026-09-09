package com.template.app.weather.bestday

import com.template.app.fake.FakeForecastRepository
import com.template.app.fake.FakeLocationRepository
import com.template.app.fake.FakePreferencesRepository
import com.template.app.fake.RecordingNavControls
import com.template.app.fake.dailyForecast
import com.template.app.fake.forecast
import com.template.app.fake.geoLocation
import com.template.app.navigation.Destination
import com.template.app.text
import com.template.app.weather.bestday.BestDayModels.Action
import com.template.core.common.time.FixedClock
import com.template.domain.weather.logic.RankDaysByComfort
import com.template.domain.weather.logic.RankSavedLocationDays
import com.template.domain.weather.logic.ScoreDayComfort
import com.template.domain.weather.model.ActivityProfile
import com.template.feature.settings.model.UnitSystem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * `stateFlowMode` is `WhileSubscribed`, so state is only computed while something collects.
 * Collecting on `backgroundScope` is the trick — a test that only reads `viewModel.state`
 * sees `initialState()` forever.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BestDayViewModelTest {

    private val coolDay = dailyForecast(date = LocalDate(2026, 6, 1), maxTemperatureC = 14.0)
    private val warmDay = dailyForecast(date = LocalDate(2026, 6, 2), maxTemperatureC = 24.0)

    private val locationRepository = FakeLocationRepository(listOf(geoLocation(name = "Gothenburg")))
    private val forecastRepository = FakeForecastRepository()
    private val preferencesRepository = FakePreferencesRepository()
    private val navControls = RecordingNavControls()

    /** Pins "today" to the first of the two forecast days, so the labels are assertable. */
    private val clock = FixedClock(coolDay.date)

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel() = BestDayViewModel(
        rankSavedLocationDays = RankSavedLocationDays(
            locationRepository = locationRepository,
            forecastRepository = forecastRepository,
            rankDaysByComfort = RankDaysByComfort(ScoreDayComfort()),
        ),
        locationRepository = locationRepository,
        forecastRepository = forecastRepository,
        preferencesRepository = preferencesRepository,
        navControls = navControls,
        clock = clock,
    )

    /** Keeps `stateFlow` hot for the test, the way a visible screen would. */
    private fun CoroutineScope.subscribe(viewModel: BestDayViewModel) {
        launch { viewModel.stateFlow.collect { } }
    }

    @Test
    fun `with nothing cached there is nothing to rank`() = runTest(UnconfinedTestDispatcher()) {
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        assertTrue(viewModel.state.ranked.isEmpty())
        assertEquals(ActivityProfile.Running, viewModel.state.activity)
    }

    @Test
    fun `opening the screen refreshes the saved locations`() = runTest(UnconfinedTestDispatcher()) {
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        // `onStarted` runs once when collection begins.
        assertEquals(1, forecastRepository.refreshCount)
    }

    @Test
    fun `ranking puts the day that suits the activity first`() = runTest(UnconfinedTestDispatcher()) {
        forecastRepository.emit(forecast(days = listOf(warmDay, coolDay)))
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        // Running's ideal is 14°C, so the cool day must win despite being listed second.
        assertEquals("1-${coolDay.date}", viewModel.state.ranked.first().key)
        assertTrue(
            viewModel.state.ranked.first().score.value > viewModel.state.ranked.last().score.value,
        )
    }

    @Test
    fun `changing the activity re-ranks the same days`() = runTest(UnconfinedTestDispatcher()) {
        forecastRepository.emit(forecast(days = listOf(coolDay, warmDay)))
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        viewModel.onAction(Action.OnActivityChange(ActivityProfile.Picnic))

        assertEquals(ActivityProfile.Picnic, viewModel.state.activity)
        // Picnic's ideal is 24°C, so the order from the previous test inverts.
        assertEquals("1-${warmDay.date}", viewModel.state.ranked.first().key)
    }

    @Test
    fun `every saved location contributes its days`() = runTest(UnconfinedTestDispatcher()) {
        locationRepository.saved.value = listOf(
            geoLocation(id = 1L, name = "Gothenburg"),
            geoLocation(id = 2L, name = "Oslo"),
        )
        forecastRepository.emit(forecast(locationId = 1L, days = listOf(coolDay)))
        forecastRepository.emit(forecast(locationId = 2L, days = listOf(warmDay)))
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        assertEquals(2, viewModel.state.ranked.size)
        assertEquals(
            setOf("Gothenburg", "Oslo"),
            viewModel.state.ranked.map { it.place.text() }.toSet(),
        )
    }

    @Test
    fun `tapping a row opens that location's forecast`() = runTest(UnconfinedTestDispatcher()) {
        forecastRepository.emit(forecast(days = listOf(coolDay)))
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        viewModel.onAction(Action.OnOpenPlace(1L))

        assertEquals(listOf<Destination>(Destination.Forecast(1L)), navControls.navigated.toList())
    }

    @Test
    fun `a failed refresh becomes a message in state, not a crash`() = runTest(UnconfinedTestDispatcher()) {
        forecastRepository.refreshFailure = IllegalStateException("offline")
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        assertTrue(viewModel.state.error != null, "the ViewModel is the layer that catches")
    }

    @Test
    fun `the day label comes from the injected clock`() = runTest(UnconfinedTestDispatcher()) {
        forecastRepository.emit(forecast(days = listOf(coolDay, warmDay)))
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        assertEquals("day_today", viewModel.state.ranked.first { it.key.endsWith("${coolDay.date}") }.dayLabel.text())
        assertEquals("day_tomorrow", viewModel.state.ranked.first { it.key.endsWith("${warmDay.date}") }.dayLabel.text())
    }

    @Test
    fun `the unit preference reaches the formatted state`() = runTest(UnconfinedTestDispatcher()) {
        forecastRepository.emit(forecast(days = listOf(coolDay)))
        preferencesRepository.units.value = UnitSystem.Imperial
        val viewModel = viewModel()
        backgroundScope.subscribe(viewModel)

        // 14°C is 57°F. The screen renders whatever this says; it converts nothing itself.
        assertEquals("57°", viewModel.state.ranked.first().temperature.text())
    }
}
