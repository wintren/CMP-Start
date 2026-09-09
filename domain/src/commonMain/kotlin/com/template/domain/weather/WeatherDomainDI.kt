package com.template.domain.weather

import com.template.domain.weather.logic.RankDaysByComfort
import com.template.domain.weather.logic.RankSavedLocationDays
import com.template.domain.weather.logic.ScoreDayComfort
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI lives with the area, not in a central `di/` package — you edit bindings in the folder you are
 * already editing, and moving the area moves its wiring with it.
 */
val weatherDomainModule = module {
    singleOf(::ScoreDayComfort)
    singleOf(::RankDaysByComfort)
    singleOf(::RankSavedLocationDays)
}
