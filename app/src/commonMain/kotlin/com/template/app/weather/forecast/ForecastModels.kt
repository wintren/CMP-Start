package com.template.app.weather.forecast

import com.template.core.ui.resource.StringValue
import com.template.domain.weather.model.WeatherCondition

object ForecastModels {

    data class State(
        val title: StringValue = StringValue.Empty,
        val region: StringValue = StringValue.Empty,
        val isLoading: Boolean = true,
        val current: CurrentBlock? = null,
        val days: List<DayItem> = emptyList(),
        val error: StringValue? = null,
    )

    data class CurrentBlock(
        val temperature: StringValue,
        val conditionLabel: StringValue,
        val wind: StringValue,
        val condition: WeatherCondition,
    )

    data class DayItem(
        val id: Int,
        val dayLabel: StringValue,
        val condition: WeatherCondition,
        val high: StringValue,
        val low: StringValue,
        val precipitation: StringValue,
        val precipitationChance: StringValue,
        val wind: StringValue,
    )

    sealed interface Action {
        data object OnBack : Action
        data object OnRefresh : Action
    }
}
