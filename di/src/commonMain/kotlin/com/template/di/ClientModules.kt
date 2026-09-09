package com.template.di

import com.template.core.common.di.coreCommonModule
import com.template.data.dataModules
import com.template.domain.domainModules
import org.koin.core.module.Module

/** Exists so `:app` can start Koin without depending on `:data`. Nothing else belongs here. */
val clientModules: List<Module> = buildList {
    add(coreCommonModule)
    addAll(domainModules)
    addAll(dataModules)
}
