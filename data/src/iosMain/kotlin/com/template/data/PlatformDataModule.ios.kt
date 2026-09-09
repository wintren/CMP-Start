package com.template.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.template.data.database.AppDatabase
import com.template.data.weather.source.local.SavedLocationStore
import com.template.data.weather.source.local.SqlSavedLocationStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.new
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single<SqlDriver> { NativeSqliteDriver(AppDatabase.Schema, DATABASE_NAME) }
    single { AppDatabase(get<SqlDriver>()) }
    single<SavedLocationStore> { new(::SqlSavedLocationStore) }
}
