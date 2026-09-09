package com.template.launch.android

import android.app.Application
import android.content.pm.ApplicationInfo
import com.template.app.di.startAppKoin
import com.template.core.common.logging.Log
import com.template.core.common.logging.LogLevel
import org.koin.android.ext.koin.androidContext

class TemplateApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Not `BuildConfig.DEBUG`, which would mean turning `buildFeatures.buildConfig` on for one
        // boolean. Also decides whether Ktor logs full requests — see HttpClientFactory.
        Log.minimumLevel = when (isDebuggable()) {
            true -> LogLevel.Verbose
            false -> LogLevel.Warn
        }

        startAppKoin { androidContext(this@TemplateApplication) }
    }

    private fun isDebuggable(): Boolean =
        applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}
