package com.template.di

import com.template.core.common.di.coreCommonModule
import com.template.data.dataModules
import com.template.domain.domainModules
import org.koin.core.module.Module

/**
 * Everything below the UI, in one list.
 *
 * This module exists so `:app` can start Koin without depending on `:data` — the UI lane stays
 * unable to import a repository implementation even by accident. Nothing else belongs here.
 */
val clientModules: List<Module> = buildList {
    add(coreCommonModule)
    addAll(domainModules)
    addAll(dataModules)
}
