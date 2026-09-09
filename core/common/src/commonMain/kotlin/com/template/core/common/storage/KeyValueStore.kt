package com.template.core.common.storage

import kotlinx.coroutines.flow.Flow

/**
 * Small persisted key/value surface. It exists so features never see the storage library's types —
 * swapping the backing store is then one class, not a migration.
 */
interface KeyValueStore {
    fun observeString(key: String): Flow<String?>
    fun observeInt(key: String, default: Int): Flow<Int>
    fun observeBoolean(key: String, default: Boolean): Flow<Boolean>

    suspend fun getString(key: String): String?

    suspend fun putString(key: String, value: String?)
    suspend fun putInt(key: String, value: Int)
    suspend fun putBoolean(key: String, value: Boolean)
}
