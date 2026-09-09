package com.template.core.common.di

import com.template.core.common.storage.KeyValueStore
import com.template.core.common.storage.SettingsKeyValueStore
import com.template.core.common.storage.platformSettings
import kotlin.time.Clock
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val coreCommonModule = module {
    single { platformSettings() }
    single<KeyValueStore> { new(::SettingsKeyValueStore) }

    // The one place the system clock is read. Everything else takes a `Clock` and gets a fixed
    // one in tests — see core/common/time/Time.kt.
    single<Clock> { Clock.System }
}
