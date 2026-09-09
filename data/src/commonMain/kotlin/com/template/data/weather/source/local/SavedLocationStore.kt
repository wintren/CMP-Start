package com.template.data.weather.source.local

import kotlinx.coroutines.flow.Flow

/**
 * An interface with injected impls rather than `expect`/`actual`: [SqlSavedLocationStore] and
 * [JsonSavedLocationStore] share no shape. Bound in `PlatformDataModule`.
 */
internal interface SavedLocationStore {
    fun observe(): Flow<List<SavedLocationEntity>>
    suspend fun get(): List<SavedLocationEntity>
    suspend fun upsert(entity: SavedLocationEntity)
    suspend fun remove(id: Long)
}
