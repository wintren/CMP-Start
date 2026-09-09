package com.template.data

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.template.data.database.AppDatabase
import com.template.data.weather.source.local.SavedLocationStore
import com.template.data.weather.source.local.SqlSavedLocationStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.new
import org.koin.dsl.module
import java.io.File

actual val platformDataModule: Module = module {
    single<SqlDriver> { desktopDriver() }
    single { AppDatabase(get<SqlDriver>()) }
    single<SavedLocationStore> { new(::SqlSavedLocationStore) }
}

/**
 * The JDBC driver tracks no schema version — it opens a v1 file against v2 code and fails on
 * the first query. So create and migrate here, off `PRAGMA user_version`.
 */
private fun desktopDriver(): SqlDriver {
    val file = File(System.getProperty("user.home"), ".CMP_Start/$DATABASE_NAME")
    file.parentFile?.mkdirs()

    val driver = JdbcSqliteDriver("jdbc:sqlite:${file.absolutePath}")
    val target = AppDatabase.Schema.version
    val current = driver.executeQuery(
        identifier = null,
        sql = "PRAGMA user_version;",
        mapper = { cursor -> QueryResult.Value(cursor.getLong(0) ?: 0L) },
        parameters = 0,
    ).value

    when {
        current == 0L -> AppDatabase.Schema.create(driver)
        current < target -> AppDatabase.Schema.migrate(driver, current, target)
        else -> return driver
    }
    driver.execute(null, "PRAGMA user_version = $target;", 0)
    return driver
}
