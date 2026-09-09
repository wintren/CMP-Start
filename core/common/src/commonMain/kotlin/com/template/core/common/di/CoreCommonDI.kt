package com.template.core.common.di

import com.template.core.common.storage.KeyValueStore
import com.template.core.common.storage.SettingsKeyValueStore
import com.template.core.common.storage.platformSettings
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val coreCommonModule = module {
    single { platformSettings() }
    // `new(::Impl)` registers only the interface — the impl stays unresolvable in the graph.
    single<KeyValueStore> { new(::SettingsKeyValueStore) }
}
