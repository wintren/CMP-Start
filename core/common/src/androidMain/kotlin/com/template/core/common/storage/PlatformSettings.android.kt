package com.template.core.common.storage

import com.russhwolf.settings.Settings

// `Settings()` comes from multiplatform-settings-no-arg, which resolves the platform default for us.
actual fun platformSettings(): Settings = Settings()
