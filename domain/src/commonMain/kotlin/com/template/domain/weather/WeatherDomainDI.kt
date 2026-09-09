package com.template.domain.weather

import com.template.domain.weather.logic.RankDaysByComfort
import com.template.domain.weather.logic.RankSavedLocationDays
import com.template.domain.weather.logic.ScoreDayComfort
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val weatherDomainModule = module {
    singleOf(::ScoreDayComfort)
    singleOf(::RankDaysByComfort)
    singleOf(::RankSavedLocationDays)
}
