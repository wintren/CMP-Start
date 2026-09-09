package com.template.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.template.data.database.AppDatabase
import com.template.data.weather.source.local.SavedLocationStore
import com.template.data.weather.source.local.SqlSavedLocationStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.new
import org.koin.dsl.module

// AndroidSqliteDriver runs Schema.create and Schema.migrate itself, off the file's version.
// `androidContext(...)` in the Application is what makes `get<Context>()` work.
actual val platformDataModule: Module = module {
    single<SqlDriver> { AndroidSqliteDriver(AppDatabase.Schema, get<Context>(), DATABASE_NAME) }
    single { AppDatabase(get<SqlDriver>()) }
    single<SavedLocationStore> { new(::SqlSavedLocationStore) }
}
