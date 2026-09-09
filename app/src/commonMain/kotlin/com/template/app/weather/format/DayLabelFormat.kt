package com.template.app.weather.format

import com.template.app.resources.Res
import com.template.app.resources.date_full
import com.template.app.resources.day_and_number
import com.template.app.resources.day_today
import com.template.app.resources.day_tomorrow
import com.template.app.resources.month_apr
import com.template.app.resources.month_aug
import com.template.app.resources.month_dec
import com.template.app.resources.month_feb
import com.template.app.resources.month_jan
import com.template.app.resources.month_jul
import com.template.app.resources.month_jun
import com.template.app.resources.month_mar
import com.template.app.resources.month_may
import com.template.app.resources.month_nov
import com.template.app.resources.month_oct
import com.template.app.resources.month_sep
import com.template.app.resources.weekday_fri
import com.template.app.resources.weekday_mon
import com.template.app.resources.weekday_sat
import com.template.app.resources.weekday_sun
import com.template.app.resources.weekday_thu
import com.template.app.resources.weekday_tue
import com.template.app.resources.weekday_wed
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

/**
 * "Today" / "Tomorrow" / "Wed 17", still unresolved. Takes [today] as a parameter instead of
 * reading a clock, so the output is a function of its inputs and a test can pin the date.
 *
 * The weekday and month names are string resources rather than `DayOfWeek.name`: kotlinx-datetime
 * has no locale-aware formatting in common code, and the enum name is English by definition.
 */
fun LocalDate.asDayLabel(today: LocalDate): StringValue = when (this) {
    today -> Res.string.day_today.asValue()
    today.plusDaysSafely(1) -> Res.string.day_tomorrow.asValue()
    else -> Res.string.day_and_number.asValue(dayOfWeek.shortName(), StringValue.of(day))
}

fun LocalDate.asFullDate(): StringValue =
    Res.string.date_full.asValue(dayOfWeek.shortName(), StringValue.of(day), month.shortName())

private fun LocalDate.plusDaysSafely(days: Int): LocalDate =
    LocalDate.fromEpochDays(toEpochDays() + days)

private fun DayOfWeek.shortName(): StringValue = when (this) {
    DayOfWeek.MONDAY -> Res.string.weekday_mon
    DayOfWeek.TUESDAY -> Res.string.weekday_tue
    DayOfWeek.WEDNESDAY -> Res.string.weekday_wed
    DayOfWeek.THURSDAY -> Res.string.weekday_thu
    DayOfWeek.FRIDAY -> Res.string.weekday_fri
    DayOfWeek.SATURDAY -> Res.string.weekday_sat
    DayOfWeek.SUNDAY -> Res.string.weekday_sun
}.asValue()

private fun Month.shortName(): StringValue = when (this) {
    Month.JANUARY -> Res.string.month_jan
    Month.FEBRUARY -> Res.string.month_feb
    Month.MARCH -> Res.string.month_mar
    Month.APRIL -> Res.string.month_apr
    Month.MAY -> Res.string.month_may
    Month.JUNE -> Res.string.month_jun
    Month.JULY -> Res.string.month_jul
    Month.AUGUST -> Res.string.month_aug
    Month.SEPTEMBER -> Res.string.month_sep
    Month.OCTOBER -> Res.string.month_oct
    Month.NOVEMBER -> Res.string.month_nov
    Month.DECEMBER -> Res.string.month_dec
}.asValue()
