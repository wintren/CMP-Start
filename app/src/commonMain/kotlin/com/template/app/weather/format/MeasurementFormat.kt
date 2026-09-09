package com.template.app.weather.format

import com.template.core.ui.resource.StringValue
import com.template.feature.settings.model.UnitSystem
import kotlin.math.roundToInt

/** Conversion lives at the presentation edge; the domain has exactly one unit system. */
fun Double.asTemperature(units: UnitSystem): StringValue = when (units) {
    UnitSystem.Metric -> StringValue.Raw("${roundToInt()}°C")
    UnitSystem.Imperial -> StringValue.Raw("${(this * 9 / 5 + 32).roundToInt()}°F")
}

/** Without the unit suffix, for a row where the unit is already in the header. */
fun Double.asTemperatureValue(units: UnitSystem): StringValue = when (units) {
    UnitSystem.Metric -> StringValue.Raw("${roundToInt()}°")
    UnitSystem.Imperial -> StringValue.Raw("${(this * 9 / 5 + 32).roundToInt()}°")
}

fun Double.asWindSpeed(units: UnitSystem): StringValue = when (units) {
    UnitSystem.Metric -> StringValue.Raw("${roundToInt()} m/s")
    UnitSystem.Imperial -> StringValue.Raw("${(this * MS_TO_MPH).roundToInt()} mph")
}

fun Double.asPrecipitation(units: UnitSystem): StringValue = when (units) {
    UnitSystem.Metric -> StringValue.Raw("${oneDecimal(this)} mm")
    UnitSystem.Imperial -> StringValue.Raw("${oneDecimal(this / MM_PER_INCH)} in")
}

private fun oneDecimal(value: Double): String {
    val scaled = (value * 10).roundToInt()
    return "${scaled / 10}.${(if (scaled < 0) -scaled else scaled) % 10}"
}

private const val MS_TO_MPH = 2.236936
private const val MM_PER_INCH = 25.4
