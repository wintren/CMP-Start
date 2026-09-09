package com.template.data.weather.source.local

import kotlinx.coroutines.flow.Flow

/**
 * The saved list, however this platform can persist it.
 *
 * An interface with an injected implementation rather than `expect`/`actual`, because the two
 * implementations share no shape: [SqlSavedLocationStore] on the three targets with SQLite, and
 * [JsonSavedLocationStore] on wasmJs, which has none. The binding is in `PlatformDataModule`.
 */
internal interface SavedLocationStore {
    fun observe(): Flow<List<SavedLocationEntity>>
    suspend fun get(): List<SavedLocationEntity>
    suspend fun upsert(entity: SavedLocationEntity)
    suspend fun remove(id: Long)
}
