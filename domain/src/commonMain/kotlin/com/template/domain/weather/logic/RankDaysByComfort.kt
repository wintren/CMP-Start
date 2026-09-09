package com.template.domain.weather.logic

import com.template.domain.weather.model.ComfortProfile
import com.template.domain.weather.model.DailyForecast
import com.template.domain.weather.model.RankedDay

/** Ties keep calendar order, so the soonest good day wins. */
class RankDaysByComfort(private val scoreDayComfort: ScoreDayComfort) {

    operator fun invoke(days: List<DailyForecast>, profile: ComfortProfile): List<RankedDay> =
        days.map { day -> RankedDay(day = day, score = scoreDayComfort(day, profile)) }
            .sortedWith(compareByDescending<RankedDay> { it.score.value }.thenBy { it.day.date })
}
