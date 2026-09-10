package com.template.app.navigation

import com.template.app.fake.FakeKeyValueStore
import com.template.core.common.time.FixedClock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class BackStackStoreTest {

    private val store = FakeKeyValueStore()
    private val clock = FixedClock(Instant.parse("2026-09-09T10:00:00Z"))
    private val backStack = BackStackStore(store = store, clock = clock, staleAfter = 30.minutes)

    private val stack = listOf(Destination.Locations, Destination.Forecast(42L))

    @Test
    fun `nothing saved is nothing to restore`() = runTest {
        assertNull(backStack.restored())
    }

    @Test
    fun `coming back within the window resumes where you were`() = runTest {
        backStack.save(stack)
        clock.instant += 29.minutes

        assertEquals(stack, backStack.restored())
    }

    @Test
    fun `coming back tomorrow starts fresh`() = runTest {
        backStack.save(stack)
        clock.instant += 4.hours

        assertNull(backStack.restored())
    }

    @Test
    fun `a clock that moved backwards is not trusted`() = runTest {
        backStack.save(stack)
        clock.instant -= 1.hours

        assertNull(backStack.restored())
    }

    @Test
    fun `a stack of destinations this version dropped is not restored`() = runTest {
        store.putString("navigation.routes", "retired-screen")
        store.putString("navigation.savedAt", clock.now().toEpochMilliseconds().toString())

        assertNull(backStack.restored())
    }
}
