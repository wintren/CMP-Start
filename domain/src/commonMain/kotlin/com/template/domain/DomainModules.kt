package com.template.domain

import com.template.domain.weather.weatherDomainModule

/** Thin aggregator so `:di` gathers one entry per module rather than one per area. */
val domainModules = listOf(weatherDomainModule)
