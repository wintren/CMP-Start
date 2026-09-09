package com.template.core.common.storage

import kotlinx.coroutines.flow.Flow

/** Features never see the storage library's types, so swapping it is one class. */
interface KeyValueStore {
    fun observeString(key: String): Flow<String?>
    fun observeInt(key: String, default: Int): Flow<Int>
    fun observeBoolean(key: String, default: Boolean): Flow<Boolean>

    suspend fun getString(key: String): String?
    suspend fun getInt(key: String, default: Int): Int
    suspend fun getBoolean(key: String, default: Boolean): Boolean

    suspend fun putString(key: String, value: String?)
    suspend fun putInt(key: String, value: Int)
    suspend fun putBoolean(key: String, value: Boolean)
}
