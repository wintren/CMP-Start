package com.template.core.common.storage

import com.russhwolf.settings.Settings

/** SharedPreferences, NSUserDefaults, java.util.prefs or `localStorage`. */
expect fun platformSettings(): Settings
