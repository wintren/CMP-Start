package com.template.core.common.storage

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * [KeyValueStore] over multiplatform-settings.
 *
 * Reactivity comes from a local revision counter rather than the library's `ObservableSettings`,
 * because observability is not available on every target this project builds for (notably wasmJs).
 * Every write goes through this class, so the counter sees all in-app changes; an edit made by
 * another process would not be observed.
 */
class SettingsKeyValueStore(private val settings: Settings) : KeyValueStore {

    private val revision = MutableStateFlow(0)

    override fun observeString(key: String): Flow<String?> =
        observe { settings.getStringOrNull(key) }

    override fun observeInt(key: String, default: Int): Flow<Int> =
        observe { settings.getInt(key, default) }

    override fun observeBoolean(key: String, default: Boolean): Flow<Boolean> =
        observe { settings.getBoolean(key, default) }

    override suspend fun getString(key: String): String? = settings.getStringOrNull(key)

    override suspend fun putString(key: String, value: String?) = write {
        if (value == null) settings.remove(key) else settings.putString(key, value)
    }

    override suspend fun putInt(key: String, value: Int) = write { settings.putInt(key, value) }

    override suspend fun putBoolean(key: String, value: Boolean) = write {
        settings.putBoolean(key, value)
    }

    private fun <T> observe(read: () -> T): Flow<T> =
        revision.map { read() }.distinctUntilChanged()

    private inline fun write(block: () -> Unit) {
        block()
        revision.update { it + 1 }
    }
}
