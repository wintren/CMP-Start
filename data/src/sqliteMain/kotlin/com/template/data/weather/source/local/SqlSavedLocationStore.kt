package com.template.data.weather.source.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.template.data.database.AppDatabase
import com.template.data.database.SavedLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * SQLDelight's generated row type stays in here. The interface deals in [SavedLocationEntity], so
 * a schema change is a change to this file and its migration, not to anything that reads the list.
 */
internal class SqlSavedLocationStore(database: AppDatabase) : SavedLocationStore {

    private val queries = database.savedLocationQueries

    override fun observe(): Flow<List<SavedLocationEntity>> = queries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.Default)
        .map { rows -> rows.map { it.toEntity() } }

    override suspend fun get(): List<SavedLocationEntity> = withContext(Dispatchers.Default) {
        queries.selectAll().executeAsList().map { it.toEntity() }
    }

    override suspend fun upsert(entity: SavedLocationEntity): Unit = withContext(Dispatchers.Default) {
        queries.upsert(
            id = entity.id,
            name = entity.name,
            country = entity.country,
            region = entity.region,
            latitude = entity.latitude,
            longitude = entity.longitude,
            time_zone = entity.timeZone,
        )
    }

    override suspend fun remove(id: Long): Unit = withContext(Dispatchers.Default) {
        queries.delete(id)
    }
}

private fun SavedLocation.toEntity() = SavedLocationEntity(
    id = id,
    name = name,
    country = country,
    region = region,
    latitude = latitude,
    longitude = longitude,
    timeZone = time_zone,
)
