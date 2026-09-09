package com.template.app.weather.format

import com.template.core.ui.resource.StringValue
import kotlinx.datetime.LocalDate

/**
 * "Today" / "Tomorrow" / "Wed 17". Takes [today] as a parameter instead of reading a clock, so the
 * output is a function of its inputs and a test can pin the date.
 */
fun LocalDate.asDayLabel(today: LocalDate): StringValue = when (this) {
    today -> StringValue.Raw("Today")
    today.plusDaysSafely(1) -> StringValue.Raw("Tomorrow")
    else -> StringValue.Raw("${dayOfWeek.shortName()} $day")
}

fun LocalDate.asFullDate(): StringValue =
    StringValue.Raw("${dayOfWeek.shortName()} $day ${month.shortName()}")

private fun LocalDate.plusDaysSafely(days: Int): LocalDate =
    LocalDate.fromEpochDays(toEpochDays() + days)

private fun kotlinx.datetime.DayOfWeek.shortName(): String =
    name.take(3).lowercase().replaceFirstChar { it.uppercase() }

private fun kotlinx.datetime.Month.shortName(): String =
    name.take(3).lowercase().replaceFirstChar { it.uppercase() }
