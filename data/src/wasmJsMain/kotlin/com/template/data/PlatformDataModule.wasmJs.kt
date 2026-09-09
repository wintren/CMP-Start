package com.template.data

import com.template.data.weather.source.local.JsonSavedLocationStore
import com.template.data.weather.source.local.SavedLocationStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.new
import org.koin.dsl.module

// No SQLite here: a browser build would need an sql.js worker asset and a webpack rule to serve
// it, which is not something a starter should carry. The list goes to `localStorage` instead.
actual val platformDataModule: Module = module {
    single<SavedLocationStore> { new(::JsonSavedLocationStore) }
}
