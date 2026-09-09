package com.template.app.weather.format

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.template.app.resources.Res
import com.template.app.resources.condition_clear
import com.template.app.resources.condition_drizzle
import com.template.app.resources.condition_fog
import com.template.app.resources.condition_overcast
import com.template.app.resources.condition_partly_cloudy
import com.template.app.resources.condition_rain
import com.template.app.resources.condition_snow
import com.template.app.resources.condition_thunderstorm
import com.template.app.resources.condition_unknown
import com.template.app.resources.penalty_poor_visibility
import com.template.app.resources.penalty_storm_risk
import com.template.app.resources.penalty_too_cold
import com.template.app.resources.penalty_too_warm
import com.template.app.resources.penalty_wet
import com.template.app.resources.penalty_windy
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
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

fun WeatherCondition.label(): StringValue = when (this) {
    WeatherCondition.Clear -> Res.string.condition_clear
    WeatherCondition.PartlyCloudy -> Res.string.condition_partly_cloudy
    WeatherCondition.Overcast -> Res.string.condition_overcast
    WeatherCondition.Fog -> Res.string.condition_fog
    WeatherCondition.Drizzle -> Res.string.condition_drizzle
    WeatherCondition.Rain -> Res.string.condition_rain
    WeatherCondition.Snow -> Res.string.condition_snow
    WeatherCondition.Thunderstorm -> Res.string.condition_thunderstorm
    WeatherCondition.Unknown -> Res.string.condition_unknown
}.asValue()

fun ComfortPenalty.label(): StringValue = when (this) {
    ComfortPenalty.TooCold -> Res.string.penalty_too_cold
    ComfortPenalty.TooWarm -> Res.string.penalty_too_warm
    ComfortPenalty.Windy -> Res.string.penalty_windy
    ComfortPenalty.Wet -> Res.string.penalty_wet
    ComfortPenalty.PoorVisibility -> Res.string.penalty_poor_visibility
    ComfortPenalty.StormRisk -> Res.string.penalty_storm_risk
}.asValue()
