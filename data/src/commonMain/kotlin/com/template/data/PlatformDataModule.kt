package com.template.data

import org.koin.core.module.Module

/**
 * What `:data` can only wire per platform: the SQLite driver, and therefore which
 * `SavedLocationStore` this target gets.
 *
 * `expect`/`actual` on the Koin module rather than on the driver, because the drivers do not share
 * a constructor — Android's needs a `Context`, desktop's needs a file path and runs the migration
 * itself, and wasmJs has no driver at all.
 */
expect val platformDataModule: Module

internal const val DATABASE_NAME = "app.db"
