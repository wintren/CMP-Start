package com.template.core.common.time

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeTest {

    private val zone = TimeZone.UTC

    /** A Wednesday. */
    private val wednesday = LocalDate(2026, 9, 9)

    private val clock = FixedClock(wednesday, zone)

    @Test
    fun `the clock decides what today is`() {
        assertEquals(wednesday, clock.today(zone))
        assertEquals(LocalDate(2026, 9, 10), clock.tomorrow(zone))
        assertEquals(LocalDate(2026, 9, 8), clock.yesterday(zone))
        assertEquals(LocalDateTime(2026, 9, 9, 0, 0), clock.localNow(zone))
        assertEquals(clock.instant, clock.startOfToday(zone))
    }

    @Test
    fun `this week runs Monday to Sunday around today`() {
        val week = clock.thisWeek(zone)

        assertEquals(LocalDate(2026, 9, 7), week.start)
        assertEquals(LocalDate(2026, 9, 13), week.endInclusive)
    }

    @Test
    fun `a Sunday belongs to the week that started six days earlier`() {
        val week = LocalDate(2026, 9, 13).weekOf()

        assertEquals(LocalDate(2026, 9, 7), week.start)
        assertEquals(LocalDate(2026, 9, 13), week.endInclusive)
    }

    @Test
    fun `a week can start on Sunday instead`() {
        val week = clock.thisWeek(zone, startsOn = DayOfWeek.SUNDAY)

        assertEquals(LocalDate(2026, 9, 6), week.start)
        assertEquals(LocalDate(2026, 9, 12), week.endInclusive)
    }

    @Test
    fun `the next seven days begin with today`() {
        val days = clock.nextDays(count = 7, zone = zone)

        assertEquals(7, days.size)
        assertEquals(wednesday, days.first())
        assertEquals(LocalDate(2026, 9, 15), days.last())
    }

    @Test
    fun `moving the clock moves today`() {
        val moving = FixedClock(wednesday, zone)

        moving.instant = LocalDate(2027, 1, 1).atStartOfDayIn(zone)

        assertEquals(LocalDate(2027, 1, 1), moving.today(zone))
    }
}
