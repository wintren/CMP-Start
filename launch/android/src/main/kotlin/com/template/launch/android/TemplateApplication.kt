package com.template.launch.android

import android.app.Application
import com.template.app.di.startAppKoin
import org.koin.android.ext.koin.androidContext

class TemplateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startAppKoin { androidContext(this@TemplateApplication) }
    }
}
