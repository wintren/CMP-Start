package com.template.data.weather.source.local

import com.template.core.common.logging.Log
import com.template.core.common.storage.KeyValueStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

/**
 * Persists the saved list as one JSON blob.
 *
 * Fine for a handful of rows, and it keeps the starter free of a database and its schema
 * migrations. Swap in SQLDelight the moment you need queries, partial reads or more than a few
 * hundred rows — this class is the only thing that changes.
 */
internal class SavedLocationStore(
    private val keyValueStore: KeyValueStore,
    private val json: Json,
) {

    fun observe(): Flow<List<SavedLocationEntity>> =
        keyValueStore.observeString(KEY).map(::decode)

    suspend fun get(): List<SavedLocationEntity> = decode(keyValueStore.getString(KEY))

    suspend fun upsert(entity: SavedLocationEntity) {
        val current = get()
        // Re-saving a place must not duplicate it, and must not reorder the list.
        val next = when (current.any { it.id == entity.id }) {
            true -> current.map { if (it.id == entity.id) entity else it }
            false -> current + entity
        }
        write(next)
    }

    suspend fun remove(id: Long) = write(get().filterNot { it.id == id })

    private suspend fun write(entities: List<SavedLocationEntity>) =
        keyValueStore.putString(KEY, json.encodeToString(entities))

    private fun decode(raw: String?): List<SavedLocationEntity> {
        if (raw == null) return emptyList()
        return runCatching { json.decodeFromString<List<SavedLocationEntity>>(raw) }
            .onFailure { Log.w(TAG) { "Dropping unreadable saved list: ${it.message}" } }
            .getOrDefault(emptyList())
    }

    private companion object {
        const val KEY = "weather.saved_locations"
        const val TAG = "SavedLocationStore"
    }
}
