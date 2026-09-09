package com.template.data.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pins `migrations/1.sqm`. Builds the v1 schema by hand — the migration's whole job is to open a
 * database this code no longer knows how to create — then migrates and checks the row came with
 * it. A rename that silently dropped the column would still leave the schema valid, so asserting
 * on the *data* is the point.
 *
 * JVM-only because it needs a real driver; the migration itself is platform-independent SQL.
 */
class SavedLocationMigrationTest {

    @Test
    fun `a v1 database keeps its rows through the rename`() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

        driver.execute(
            identifier = null,
            sql = """
                CREATE TABLE savedLocation (
                    id INTEGER NOT NULL PRIMARY KEY,
                    place_name TEXT NOT NULL,
                    country TEXT,
                    region TEXT,
                    latitude REAL NOT NULL,
                    longitude REAL NOT NULL,
                    time_zone TEXT NOT NULL
                );
            """.trimIndent(),
            parameters = 0,
        )
        driver.execute(
            identifier = null,
            sql = "INSERT INTO savedLocation VALUES (1, 'Gothenburg', 'SE', NULL, 57.7, 12.0, 'Europe/Stockholm');",
            parameters = 0,
        )

        AppDatabase.Schema.migrate(driver, oldVersion = 1L, newVersion = AppDatabase.Schema.version)

        val saved = AppDatabase(driver).savedLocationQueries.selectAll().executeAsOne()
        assertEquals("Gothenburg", saved.name)
        assertEquals("Europe/Stockholm", saved.time_zone)

        val version = driver.executeQuery(
            identifier = null,
            sql = "SELECT count(*) FROM savedLocation;",
            mapper = { QueryResult.Value(it.getLong(0)) },
            parameters = 0,
        ).value
        assertEquals(1L, version)
    }
}
