package com.template.core.common.storage

import com.russhwolf.settings.Settings

/**
 * The platform's default persisted store: SharedPreferences, NSUserDefaults, java.util.prefs or
 * `localStorage`. Called once, from [com.template.core.common.di.coreCommonModule].
 */
expect fun platformSettings(): Settings
