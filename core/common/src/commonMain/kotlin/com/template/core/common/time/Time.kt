package com.template.core.common.time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

/*
 * The clock is the receiver, not a hidden `Clock.System` read, so a test can pin "now" by passing
 * a `FixedClock`. `:archtest` fails `Clock.System` anywhere but the Koin binding, which is what
 * stops a shorter-looking `today()` from appearing later and taking the determinism back out.
 *
 * `now()` is stdlib — `Clock.now(): Instant`. These are the calendar answers it does not give.
 */

fun Clock.today(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate = todayIn(zone)

fun Clock.tomorrow(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    today(zone).plus(1, DateTimeUnit.DAY)

fun Clock.yesterday(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    today(zone).minus(1, DateTimeUnit.DAY)

fun Clock.localNow(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    now().toLocalDateTime(zone)

fun Clock.startOfToday(zone: TimeZone = TimeZone.currentSystemDefault()): Instant =
    today(zone).atStartOfDayIn(zone)

fun Clock.thisWeek(
    zone: TimeZone = TimeZone.currentSystemDefault(),
    startsOn: DayOfWeek = DayOfWeek.MONDAY,
): ClosedRange<LocalDate> = today(zone).weekOf(startsOn)

/** [count] dates beginning with today, the shape a forecast or an agenda asks for. */
fun Clock.nextDays(count: Int, zone: TimeZone = TimeZone.currentSystemDefault()): List<LocalDate> {
    val today = today(zone)
    return List(count) { today.plus(it, DateTimeUnit.DAY) }
}

fun LocalDate.weekOf(startsOn: DayOfWeek = DayOfWeek.MONDAY): ClosedRange<LocalDate> {
    val start = minus((dayOfWeek.isoDayNumber - startsOn.isoDayNumber + DAYS_IN_WEEK) % DAYS_IN_WEEK, DateTimeUnit.DAY)
    return start..start.plus(DAYS_IN_WEEK - 1, DateTimeUnit.DAY)
}

private const val DAYS_IN_WEEK = 7
