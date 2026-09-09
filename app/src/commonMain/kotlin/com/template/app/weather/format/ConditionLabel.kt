package com.template.app.weather.format

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.template.core.ui.resource.StringValue
import com.template.domain.weather.model.ComfortPenalty
import com.template.domain.weather.model.WeatherCondition

fun WeatherCondition.icon(): ImageVector = when (this) {
    WeatherCondition.Clear -> Icons.Default.WbSunny
    WeatherCondition.PartlyCloudy -> Icons.Default.WbCloudy
    WeatherCondition.Overcast -> Icons.Default.Cloud
    WeatherCondition.Fog -> Icons.Default.Grain
    WeatherCondition.Drizzle -> Icons.Default.Umbrella
    WeatherCondition.Rain -> Icons.Default.Umbrella
    WeatherCondition.Snow -> Icons.Default.AcUnit
    WeatherCondition.Thunderstorm -> Icons.Default.Thunderstorm
    WeatherCondition.Unknown -> Icons.AutoMirrored.Filled.HelpOutline
}

fun WeatherCondition.label(): StringValue = StringValue.Raw(
    when (this) {
        WeatherCondition.Clear -> "Clear"
        WeatherCondition.PartlyCloudy -> "Partly cloudy"
        WeatherCondition.Overcast -> "Overcast"
        WeatherCondition.Fog -> "Fog"
        WeatherCondition.Drizzle -> "Drizzle"
        WeatherCondition.Rain -> "Rain"
        WeatherCondition.Snow -> "Snow"
        WeatherCondition.Thunderstorm -> "Thunderstorm"
        WeatherCondition.Unknown -> "—"
    }
)

fun ComfortPenalty.label(): StringValue = StringValue.Raw(
    when (this) {
        ComfortPenalty.TooCold -> "Cold"
        ComfortPenalty.TooWarm -> "Warm"
        ComfortPenalty.Windy -> "Windy"
        ComfortPenalty.Wet -> "Wet"
        ComfortPenalty.PoorVisibility -> "Low visibility"
        ComfortPenalty.StormRisk -> "Storms"
    }
)
