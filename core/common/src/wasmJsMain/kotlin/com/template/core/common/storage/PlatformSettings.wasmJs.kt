package com.template.core.common.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

// No `multiplatform-settings-no-arg` variant for wasmJs, so name the browser store directly.
actual fun platformSettings(): Settings = StorageSettings()
