package com.template.core.common.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * A clock that does not tick. Ships in main sources rather than a test fixture so tests in every
 * module and a `@Preview` can share one; assign [instant] to move time.
 */
class FixedClock(var instant: Instant) : Clock {

    constructor(
        date: LocalDate,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ) : this(date.atStartOfDayIn(zone))

    override fun now(): Instant = instant
}
