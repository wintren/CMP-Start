package com.template.data

import org.koin.core.module.Module

/**
 * `expect`/`actual` on the Koin module rather than the driver, because the drivers share no
 * constructor: Android's needs a `Context`, desktop's a file path, and wasmJs has no driver.
 */
expect val platformDataModule: Module

internal const val DATABASE_NAME = "app.db"
