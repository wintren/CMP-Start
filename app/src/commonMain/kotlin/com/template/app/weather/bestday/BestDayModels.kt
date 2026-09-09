package com.template.app.weather.bestday

import com.template.core.ui.resource.StringValue
import com.template.domain.weather.model.ActivityProfile
import com.template.domain.weather.model.ComfortPenalty
import com.template.domain.weather.model.ComfortScore
import com.template.domain.weather.model.WeatherCondition

object BestDayModels {

    data class State(
        val activity: ActivityProfile = ActivityProfile.default,
        val activities: List<ActivityProfile> = ActivityProfile.entries,
        val ranked: List<RankedItem> = emptyList(),
        val isRefreshing: Boolean = false,
        val error: StringValue? = null,
    )

    data class RankedItem(
        val key: String,
        val locationId: Long,
        val place: StringValue,
        val dayLabel: StringValue,
        val temperature: StringValue,
        val condition: WeatherCondition,
        val score: ComfortScore,
        val penalties: List<ComfortPenalty>,
    )

    sealed interface Action {
        data class OnActivityChange(val activity: ActivityProfile) : Action
        data class OnOpenPlace(val locationId: Long) : Action
        data object OnRefresh : Action
    }
}
