package com.template.app.fake

import com.template.core.common.storage.KeyValueStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** One map behind every accessor, so a test can seed storage the way a previous launch left it. */
class FakeKeyValueStore(values: Map<String, String> = emptyMap()) : KeyValueStore {

    private val entries = MutableStateFlow(values)

    override fun observeString(key: String): Flow<String?> = entries.map { it[key] }

    override fun observeInt(key: String, default: Int): Flow<Int> =
        entries.map { it[key]?.toIntOrNull() ?: default }

    override fun observeBoolean(key: String, default: Boolean): Flow<Boolean> =
        entries.map { it[key]?.toBooleanStrictOrNull() ?: default }

    override suspend fun getString(key: String): String? = entries.value[key]

    override suspend fun getInt(key: String, default: Int): Int =
        entries.value[key]?.toIntOrNull() ?: default

    override suspend fun getBoolean(key: String, default: Boolean): Boolean =
        entries.value[key]?.toBooleanStrictOrNull() ?: default

    override suspend fun putString(key: String, value: String?) = put(key, value)

    override suspend fun putInt(key: String, value: Int) = put(key, value.toString())

    override suspend fun putBoolean(key: String, value: Boolean) = put(key, value.toString())

    private fun put(key: String, value: String?) {
        entries.update { current -> if (value == null) current - key else current + (key to value) }
    }
}
