package com.template.domain.weather.logic

import com.template.core.common.flow.combines
import com.template.domain.weather.contract.ForecastRepository
import com.template.domain.weather.contract.LocationRepository
import com.template.domain.weather.model.ActivityProfile
import com.template.domain.weather.model.RankedLocationDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A UseCase, because it spans two repositories and sequences pure logic over the result — the one
 * job a ViewModel is not allowed to do itself.
 *
 * Contrast [RankDaysByComfort], which is pure and takes its data as arguments. A UseCase that only
 * forwarded a single repository call would be a relay and should not exist: the ViewModel would
 * read that repository directly.
 */
class RankSavedLocationDays(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val rankDaysByComfort: RankDaysByComfort,
) {

    operator fun invoke(activity: ActivityProfile): Flow<List<RankedLocationDay>> =
        combines(locationRepository.observeSaved(), forecastRepository.observeAll())
            .map { (locations, forecasts) ->
                locations
                    .flatMap { location ->
                        val forecast = forecasts[location.id] ?: return@flatMap emptyList()
                        rankDaysByComfort(forecast.days, activity.comfort).map { ranked ->
                            RankedLocationDay(
                                location = location,
                                day = ranked.day,
                                score = ranked.score,
                            )
                        }
                    }
                    .sortedWith(
                        compareByDescending<RankedLocationDay> { it.score.value }
                            .thenBy { it.day.date }
                    )
            }
}
