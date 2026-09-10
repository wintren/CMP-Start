package com.template.app.navigation

import com.template.core.common.storage.KeyValueStore
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

/**
 * Where the back stack goes so that Android killing the process, a browser reload or a desktop
 * restart puts you back where you were. Routes, not serialized objects, so the format survives a
 * refactor of [Destination] and is readable when you are debugging it.
 *
 * [staleAfter] is why this is not a bookmark: coming back within the hour means resuming, coming
 * back tomorrow means starting fresh, and nobody wants an app that opens three screens deep into
 * something they finished last week.
 */
internal class BackStackStore(
    private val store: KeyValueStore,
    private val clock: Clock,
    private val staleAfter: Duration = 30.minutes,
) {

    suspend fun save(destinations: List<Destination>) {
        store.putString(KEY_ROUTES, destinations.toRoutePath())
        store.putString(KEY_SAVED_AT, clock.now().toEpochMilliseconds().toString())
    }

    /** Null when there is nothing saved, it is too old, or none of it still resolves. */
    suspend fun restored(): List<Destination>? {
        val savedAt = store.getString(KEY_SAVED_AT)?.toLongOrNull() ?: return null
        val age = clock.now() - Instant.fromEpochMilliseconds(savedAt)
        if (age > staleAfter || age.isNegative()) return null
        return store.getString(KEY_ROUTES)
            ?.let(::backStackOf)
            ?.takeIf { it.isNotEmpty() }
    }

    private companion object {
        const val KEY_ROUTES = "navigation.routes"
        const val KEY_SAVED_AT = "navigation.savedAt"
    }
}
