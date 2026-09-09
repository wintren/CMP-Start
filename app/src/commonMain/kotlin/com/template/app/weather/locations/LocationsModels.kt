package com.template.app.weather.locations

import com.template.core.ui.resource.StringValue
import com.template.domain.weather.model.WeatherCondition

object LocationsModels {

    data class State(
        val query: String = "",
        val isSearching: Boolean = false,
        val searchResults: List<PlaceItem> = emptyList(),
        val saved: List<SavedItem> = emptyList(),
        val isRefreshing: Boolean = false,
        val error: StringValue? = null,
    ) {
        val showsSearchResults: Boolean get() = query.isNotBlank()
    }

    /**
     * A row, already formatted. The screen renders strings; it does not convert units, round
     * numbers or pick words — every one of those is a decision, and decisions belong upstream where
     * they can be tested.
     */
    data class SavedItem(
        val id: Long,
        val name: StringValue,
        val region: StringValue,
        val temperature: StringValue,
        val conditionLabel: StringValue,
        val condition: WeatherCondition,
    )

    data class PlaceItem(
        val id: Long,
        val name: StringValue,
        val region: StringValue,
    )

    sealed interface Action {
        data class OnQueryChange(val query: String) : Action
        data object OnSearchSubmit : Action
        data class OnAddPlace(val placeId: Long) : Action
        data class OnRemoveSaved(val locationId: Long) : Action
        data class OnOpenSaved(val locationId: Long) : Action
        data object OnRefresh : Action
        data object OnDismissError : Action
    }
}
